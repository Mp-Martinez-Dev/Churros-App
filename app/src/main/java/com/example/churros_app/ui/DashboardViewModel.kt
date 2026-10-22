package com.example.churros_app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // --- REGISTRO DE VENTAS E INGRESOS AUTOMÁTICOS ---
    fun registrarVenta(cantidad: Int) {
        val mision = misionActual.value ?: return
        if (mision.unidadesVendidas + cantidad > mision.metaUnidades) return

        val nuevasVendidas = mision.unidadesVendidas + cantidad
        val montoTotal = cantidad * PRECIO_CHURRO_CLP
        val nuevoIngreso = nuevasVendidas * PRECIO_CHURRO_CLP

        val misionActualizada = mision.copy(
            unidadesVendidas = nuevasVendidas,
            ingresoAcumuladoCLP = nuevoIngreso
        )

        viewModelScope.launch {
            productoDao.actualizarMisionDiaria(misionActualizada)

            val movimiento = Movimiento(
                descripcion = "Venta Rápida (+$cantidad churro${if (cantidad > 1) "s" else ""})",
                montoCLP = montoTotal,
                esIngreso = true,
                fechaHora = obtenerHoraFormateada()
            )
            productoDao.registrarMovimiento(movimiento)
        }
    }

    fun prepararNuevaTanda() {
        val mision = misionActual.value ?: return
        val nuevaMeta = mision.metaUnidades + 40
        val misionActualizada = mision.copy(metaUnidades = nuevaMeta)

        viewModelScope.launch {
            productoDao.actualizarMisionDiaria(misionActualizada)
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
    fun agregarInsumo(
        nombre: String,
        cantidad: Double,
        unidadMedida: String,
        costoPorUnidadCLP: Double = 0.0
    ) {
        viewModelScope.launch {
            val nuevoInsumo = Insumo(
                nombre = nombre,
                cantidadDisponible = cantidad,
                unidadMedida = unidadMedida,
                costoPorUnidadCLP = costoPorUnidadCLP
            )
            productoDao.insertarInsumo(nuevoInsumo)
        }
    }

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
}