package com.example.churros_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.churros_app.data.Combo
import com.example.churros_app.data.Insumo
import com.example.churros_app.data.MisionDiaria
import com.example.churros_app.data.Movimiento
import com.example.churros_app.data.ProductoDao
import com.example.churros_app.data.RecetaItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(private val productoDao: ProductoDao) : ViewModel() {

    private val PRECIO_CHURRO_CLP = 200

    private fun obtenerFechaHoyCodigo(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun obtenerHoraFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    // 1. Misión diaria activa
    val misionActual: StateFlow<MisionDiaria?> = productoDao.obtenerMisionDelDia(obtenerFechaHoyCodigo())
        .map { lista -> lista.firstOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // 2. Inventario de bodega
    val listaInsumos: StateFlow<List<Insumo>> = productoDao.obtenerTodosLosInsumos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 3. Historial continuo de movimientos (Ingresos / Egresos)
    val listaMovimientos: StateFlow<List<Movimiento>> = productoDao.obtenerTodosLosMovimientos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 4. Flujo continuo de la receta personalizada
    val listaReceta: StateFlow<List<RecetaItem>> = productoDao.obtenerTodosLosIngredientesReceta()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 5. Flujo continuo de combos persistidos desde Room
    val listaCombos: StateFlow<List<Combo>> = productoDao.obtenerTodosLosCombos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        verificarOInicializarMisionHoy()
    }

    private fun verificarOInicializarMisionHoy() {
        val fechaHoy = obtenerFechaHoyCodigo()
        viewModelScope.launch {
            val misionExistente = productoDao.obtenerMisionDelDiaDirecta(fechaHoy)
            if (misionExistente == null) {
                val nuevaMision = MisionDiaria(
                    fechaCodigo = fechaHoy,
                    metaUnidades = 40,
                    unidadesVendidas = 0,
                    ingresoAcumuladoCLP = 0
                )
                productoDao.guardarMisionDiaria(nuevaMision)
            }
        }
    }

    // --- OPERACIONES DE COMBOS DE VENTAS ---
    fun agregarCombo(nombre: String, cantidadChurros: Int, precioCLP: Int) {
        viewModelScope.launch {
            productoDao.insertarCombo(
                Combo(
                    nombre = nombre,
                    cantidadChurros = cantidadChurros,
                    precioCLP = precioCLP
                )
            )
        }
    }

    fun eliminarCombo(combo: Combo) {
        viewModelScope.launch {
            productoDao.eliminarCombo(combo)
        }
    }
    fun actualizarCombo(combo: Combo) {
        viewModelScope.launch {
            productoDao.actualizarCombo(combo)
        }
    }

    // --- 1. VENTAS ➔ HISTORIAL (Soporta Ventas Simples y Combos Personalizados) ---
    fun registrarVenta(cantidad: Int, precioPersonalizadoCLP: Int? = null) {
        val mision = misionActual.value ?: return
        if (mision.unidadesVendidas + cantidad > mision.metaUnidades) return

        val nuevasVendidas = mision.unidadesVendidas + cantidad
        val montoCobrado = precioPersonalizadoCLP ?: (cantidad * PRECIO_CHURRO_CLP)
        val nuevoIngresoAcumulado = mision.ingresoAcumuladoCLP + montoCobrado

        val misionActualizada = mision.copy(
            unidadesVendidas = nuevasVendidas,
            ingresoAcumuladoCLP = nuevoIngresoAcumulado
        )

        viewModelScope.launch {
            productoDao.actualizarMisionDiaria(misionActualizada)

            // Registra el ingreso automáticamente en Historial
            val descripcionVenta = if (precioPersonalizadoCLP != null) {
                "Venta Combo/Pack (+$cantidad churros)"
            } else {
                "Venta Rápida (+$cantidad churro${if (cantidad > 1) "s" else ""})"
            }

            val movimiento = Movimiento(
                descripcion = descripcionVenta,
                montoCLP = montoCobrado,
                esIngreso = true,
                fechaHora = obtenerHoraFormateada()
            )
            productoDao.registrarMovimiento(movimiento)
        }
    }

    // --- 2. PREPARAR TANDA -> BODEGA (Descuenta insumos de Bodega según la Receta) ---
    // --- CON CONVERSIÓN AUTOMÁTICA DE UNIDADES ---
    fun prepararNuevaTanda(cantidadTandas: Int = 1) {
        val mision = misionActual.value ?: return

        viewModelScope.launch {
            // A. Incrementa la meta/churros disponibles (+40 por tanda)
            val nuevaMeta = mision.metaUnidades + (40 * cantidadTandas)

            productoDao.actualizarMisionDiaria(mision.copy(metaUnidades = nuevaMeta))

            val recetaActual = listaReceta.value
            val insumosActuales = listaInsumos.value

            // B. Descuenta del stock de Bodega entendiendo Lt, ml, g, kg, cc
            recetaActual.forEach { ingredienteReceta ->
                val insumoEnBodega = insumosActuales.find {
                    it.nombre.equals(ingredienteReceta.nombreIngrediente, ignoreCase = true) ||
                            it.nombre.contains(ingredienteReceta.nombreIngrediente, ignoreCase = true)
                }

                if (insumoEnBodega != null) {
                    // 1. Convertir requerimiento de la receta a gramos/ml base
                    val requeridoPorTanda = ingredienteReceta.cantidadParaTanda * cantidadTandas

                    val requeridoBase = convertirACantidadBase(requeridoPorTanda, ingredienteReceta.unidadMedida)

                    // 2. Convertir stock actual de bodega a gramos/ml base
                    val stockBodegaBase = convertirACantidadBase(insumoEnBodega.cantidadDisponible, insumoEnBodega.unidadMedida)

                    // 3. Descontar en la unidad base
                    val nuevoStockBase = (stockBodegaBase - requeridoBase).coerceAtLeast(0.0)

                    // 4. Volver a convertir a la unidad guardada en bodega (Ej: Litros o Kilos)
                    val nuevoStockBodega = convertirDesdeBase(nuevoStockBase, insumoEnBodega.unidadMedida)

                    // 5. Actualizar en Room
                    productoDao.actualizarInsumo(insumoEnBodega.copy(cantidadDisponible = nuevoStockBodega))
                }
            }
        }
    }

    // --- 3. BODEGA -> HISTORIAL (Compras de Insumos como Egresos) ---
    fun agregarInsumo(
        nombre: String,
        cantidad: Double,
        unidadMedida: String,
        costoTotalCompraCLP: Int = 0
    ) {
        viewModelScope.launch {
            val nuevoInsumo = Insumo(
                nombre = nombre,
                cantidadDisponible = cantidad,
                unidadMedida = unidadMedida,
                costoPorUnidadCLP = if (cantidad > 0) costoTotalCompraCLP / cantidad else 0.0
            )
            productoDao.insertarInsumo(nuevoInsumo)

            // Si se ingresó un costo de compra, se registra automáticamente como Egreso
            if (costoTotalCompraCLP > 0) {
                val movimientoGasto = Movimiento(
                    descripcion = "Compra Insumo: $nombre ($cantidad $unidadMedida)",
                    montoCLP = costoTotalCompraCLP,
                    esIngreso = false,
                    fechaHora = obtenerHoraFormateada()
                )
                productoDao.registrarMovimiento(movimientoGasto)
            }
        }
    }

    // --- REGISTRO MANUAL DE EGRESOS / INGRESOS EXTRA ---
    fun registrarMovimientoManual(descripcion: String, monto: Int, esIngreso: Boolean) {
        viewModelScope.launch {
            val movimiento = Movimiento(
                descripcion = descripcion,
                montoCLP = monto,
                esIngreso = esIngreso,
                fechaHora = obtenerHoraFormateada()
            )
            productoDao.registrarMovimiento(movimiento)
        }
    }

    // --- EDICIÓN Y ELIMINACIÓN DE MOVIMIENTOS ---
    fun actualizarMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            productoDao.actualizarMovimiento(movimiento)
        }
    }

    fun eliminarMovimiento(movimiento: Movimiento) {
        viewModelScope.launch {
            productoDao.eliminarMovimiento(movimiento)
        }
    }

    // --- OPERACIONES BODEGA ---
    fun actualizarInsumo(insumo: Insumo) {
        viewModelScope.launch {
            productoDao.actualizarInsumo(insumo)
        }
    }

    fun eliminarInsumo(insumo: Insumo) {
        viewModelScope.launch {
            productoDao.eliminarInsumo(insumo)
        }
    }

    // --- OPERACIONES CRUD RECETA ---
    fun agregarIngredienteReceta(nombre: String, cantidad: Double, unidad: String) {
        viewModelScope.launch {
            val nuevo = RecetaItem(
                nombreIngrediente = nombre,
                cantidadParaTanda = cantidad,
                unidadMedida = unidad
            )
            productoDao.insertarIngredienteReceta(nuevo)
        }
    }

    fun actualizarIngredienteReceta(item: RecetaItem) {
        viewModelScope.launch {
            productoDao.actualizarIngredienteReceta(item)
        }
    }

    fun eliminarIngredienteReceta(item: RecetaItem) {
        viewModelScope.launch {
            productoDao.eliminarIngredienteReceta(item)
        }
    }

    // --- CONVERSIÓN DE UNIDADES (Soporta Mayúsculas/Minúsculas, gr, kl, lt, cc, etc.) ---

    fun normalizarUnidad(unidadTexto: String): String {
        val limpio = unidadTexto.lowercase().replace(Regex("[^a-z]"), "").trim()
        return when {
            limpio in listOf("kg", "kl", "kilo", "kilos", "kilogramo", "kilogramos") -> "kg"
            limpio in listOf("g", "gr", "grm", "gramo", "gramos") -> "g"
            limpio in listOf("l", "lt", "lts", "litro", "litros") -> "l"
            limpio in listOf("ml", "cc", "cm3", "mililitro", "mililitros") -> "ml"
            else -> limpio
        }
    }

    fun convertirACantidadBase(cantidad: Double, unidadTexto: String): Double {
        return when (normalizarUnidad(unidadTexto)) {
            "kg" -> cantidad * 1000.0
            "g"  -> cantidad
            "l"  -> cantidad * 1000.0
            "ml" -> cantidad
            else -> cantidad
        }
    }

    fun convertirDesdeBase(cantidadBase: Double, unidadDestinoTexto: String): Double {
        return when (normalizarUnidad(unidadDestinoTexto)) {
            "kg" -> cantidadBase / 1000.0
            "g"  -> cantidadBase
            "l"  -> cantidadBase / 1000.0
            "ml" -> cantidadBase
            else -> cantidadBase
        }
    }
    class DashboardViewModelFactory(private val productoDao: ProductoDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(productoDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}