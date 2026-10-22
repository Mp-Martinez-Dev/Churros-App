package com.example.churros_app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.churros_app.R
import com.example.churros_app.data.Insumo
import com.example.churros_app.data.Movimiento
import com.example.churros_app.data.RecetaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    var pestañaSeleccionada by remember { mutableIntStateOf(0) }
    val mision by viewModel.misionActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (pestañaSeleccionada) {
                            0 -> "Mi Negocio - Alcancía Diaria"
                            1 -> "Control de Bodega e Insumos"
                            2 -> "Historial de Ventas"
                            else -> "Receta Base & Costos"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold

                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3F51B5))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFEEF0F8)) {
                NavigationBarItem(
                    selected = pestañaSeleccionada == 0,
                    onClick = { pestañaSeleccionada = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Ventas") },
                    label = { Text("Ventas") }
                )
                NavigationBarItem(
                    selected = pestañaSeleccionada == 1,
                    onClick = { pestañaSeleccionada = 1 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Bodega") },
                    label = { Text("Bodega") }
                )
                NavigationBarItem(
                    selected = pestañaSeleccionada == 2,
                    onClick = { pestañaSeleccionada = 2 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
                NavigationBarItem(
                    selected = pestañaSeleccionada == 3,
                    onClick = { pestañaSeleccionada = 3 },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Receta") },
                    label = { Text("Receta") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (pestañaSeleccionada) {
                0 -> VistaAlcancíaDiaria(mision = mision, viewModel = viewModel)
                1 -> VistaBodegaInsumos(viewModel = viewModel)
                2 -> VistaHistorialMovimientos(viewModel = viewModel)
                3 -> VistaRecetaChurros(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AnimacionCaidaMoneda(
    ejecutar: Boolean,
    alFinalizar: () -> Unit
) {
    if (!ejecutar) return

    val animacionOffsetY = remember { Animatable(0f) }
    val animacionAlpha = remember { Animatable(1f) }

    LaunchedEffect(ejecutar) {
        // Caída continua hacia la parte superior de la imagen del cerdito
        animacionOffsetY.animateTo(
            targetValue = 85f,
            animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing)
        )
        animacionAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 150)
        )
        alFinalizar()
    }

    Box(
        modifier = Modifier
            .offset(y = animacionOffsetY.value.dp)
            .graphicsLayer(alpha = animacionAlpha.value)
            .size(32.dp)
            .background(Color(0xFFFFD700), shape = CircleShape), // Moneda Dorada
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$",
            color = Color(0xFF5D4037),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun VistaAlcancíaDiaria(mision: com.example.churros_app.data.MisionDiaria?, viewModel: DashboardViewModel) {
    var animarMoneda by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        mision?.let { m ->
            val churrosDisponibles = (m.metaUnidades - m.unidadesVendidas).coerceAtLeast(0)
            val porcentaje = if (m.metaUnidades > 0) {
                ((m.unidadesVendidas.toFloat() / m.metaUnidades.toFloat()) * 100).coerceAtMost(100f)
            } else 0f
            val metaCLP = m.metaUnidades * 200

            // 1. Lógica dinámica de colores según el avance
            val colorTargetPildora = when {
                porcentaje >= 100f -> Color(0xFF2E7D32) // Verde (Meta cumplida)
                porcentaje >= 80f  -> Color(0xFF1565C0) // Azul (Muy cerca)
                porcentaje >= 50f  -> Color(0xFFEF6C00) // Naranja (A la mitad)
                else               -> Color(0xFFD32F2F) // Rojo (Porcentaje bajo)
            }

            // 2. Transición suave del color de fondo
            val colorPildoraAnimado by animateColorAsState(
                targetValue = colorTargetPildora,
                animationSpec = tween(durationMillis = 500),
                label = "ColorPildoraProgresiva"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Misión del Día", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    // --- ALCANCÍA CON ANIMACIÓN DE MONEDA ---
                    Box(
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            AnimacionCaidaMoneda(
                                ejecutar = animarMoneda,
                                alFinalizar = { animarMoneda = false }
                            )
                        }

                        Image(
                            painter = painterResource(id = com.example.churros_app.R.drawable.cerdito),
                            contentDescription = "Alcancía Cerdito",
                            modifier = Modifier
                                .size(150.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }

                    // --- PÍLDORA DE AVANCE CON COLOR DINÁMICO ---
                    Surface(
                        color = colorPildoraAnimado, //  Aplica el color reactivo (Rojo ➔ Naranja ➔ Azul ➔ Verde)
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${porcentaje.toInt()}% Cumplido",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "•",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${m.ingresoAcumuladoCLP} CLP",
                                color = Color(0xFFFFD54F), // Dorado
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    if (porcentaje >= 100f) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "¡FELICITACIONES! META CUMPLIDA",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Progreso de Venta", fontSize = 12.sp, color = Color.Gray)
                            Text("${m.unidadesVendidas} de ${m.metaUnidades} churros", fontWeight = FontWeight.Bold)
                            Text(
                                "Quedan: $churrosDisponibles churros",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (churrosDisponibles == 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Meta Objetiva", fontSize = 12.sp, color = Color.Gray)
                            Text("$metaCLP CLP", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                        }
                    }
                }
            }

            // --- REGISTRO RÁPIDO DE VENTAS ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(" Registro Rápido de Ventas", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.registrarVenta(1)
                                animarMoneda = true
                            },
                            enabled = churrosDisponibles >= 1,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF283593))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("+1 Churro", fontWeight = FontWeight.Bold)
                                Text("$200 CLP", fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.registrarVenta(5)
                                animarMoneda = true
                            },
                            enabled = churrosDisponibles >= 5,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("+5 Churros", fontWeight = FontWeight.Bold)
                                Text("$1000 CLP", fontSize = 11.sp)
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    OutlinedButton(
                        onClick = { viewModel.prepararNuevaTanda() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Preparar Nueva Tanda (+40 churros)", color = Color.Black)
                    }
                }
            }
        }
    }
}
@Composable
fun VistaBodegaInsumos(viewModel: DashboardViewModel) {
    val listaInsumos by viewModel.listaInsumos.collectAsState()
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var insumoAEditar by remember { mutableStateOf<Insumo?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Insumo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Inventario de Bodega", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            if (listaInsumos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay insumos registrados. Haz clic en '+' para agregar uno.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listaInsumos, key = { it.id }) { insumo ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(insumo.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(
                                        "${insumo.cantidadDisponible} ${insumo.unidadMedida}",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row {
                                    IconButton(onClick = { insumoAEditar = insumo }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2))
                                    }
                                    IconButton(onClick = { viewModel.eliminarInsumo(insumo) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoCrear) {
        DialogoInsumo(
            titulo = "Agregar Nuevo Insumo",
            alGuardar = { nombre, cantidad, unidad ->
                viewModel.agregarInsumo(nombre, cantidad, unidad)
                mostrarDialogoCrear = false
            },
            alCancelar = { mostrarDialogoCrear = false }
        )
    }

    insumoAEditar?.let { insumo ->
        DialogoInsumo(
            titulo = "Editar Insumo",
            nombreInicial = insumo.nombre,
            cantidadInicial = insumo.cantidadDisponible.toString(),
            unidadInicial = insumo.unidadMedida,
            alGuardar = { nombre, cantidad, unidad ->
                viewModel.actualizarInsumo(insumo.copy(nombre = nombre, cantidadDisponible = cantidad, unidadMedida = unidad))
                insumoAEditar = null
            },
            alCancelar = { insumoAEditar = null }
        )
    }
}

@Composable
fun DialogoInsumo(
    titulo: String,
    nombreInicial: String = "",
    cantidadInicial: String = "",
    unidadInicial: String = "kg",
    alGuardar: (String, Double, String) -> Unit,
    alCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var cantidad by remember { mutableStateOf(cantidadInicial) }
    var unidad by remember { mutableStateOf(unidadInicial) }

    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre (ej. Harina, Aceite)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it },
                    label = { Text("Cantidad disponible") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad de Medida (kg, L, unidades)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cantDouble = cantidad.toDoubleOrNull() ?: 0.0
                    if (nombre.isNotBlank()) {
                        alGuardar(nombre, cantDouble, unidad)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun VistaHistorialMovimientos(viewModel: DashboardViewModel) {
    val listaMovimientos by viewModel.listaMovimientos.collectAsState()
    var mostrarDialogoNuevoMovimiento by remember { mutableStateOf(false) }
    var movimientoAEditar by remember { mutableStateOf<Movimiento?>(null) }

    val totalIngresos = listaMovimientos.filter { it.esIngreso }.sumOf { it.montoCLP }
    val totalEgresos = listaMovimientos.filter { !it.esIngreso }.sumOf { it.montoCLP }
    val balanceNeto = totalIngresos - totalEgresos

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoNuevoMovimiento = true },
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Movimiento")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(" Resumen del Negocio", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Ingresos", fontSize = 12.sp, color = Color.Gray)
                        Text("+$totalIngresos CLP", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Egresos (Gastos)", fontSize = 12.sp, color = Color.Gray)
                        Text("-$totalEgresos CLP", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Balance Total", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            "$balanceNeto CLP",
                            color = if (balanceNeto >= 0) Color(0xFF1565C0) else Color(0xFFC62828),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(" Historial de Transacciones", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            if (listaMovimientos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay movimientos registrados.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listaMovimientos, key = { it.id }) { mov ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(mov.descripcion, fontWeight = FontWeight.Bold)
                                    Text(
                                        mov.obtenerFechaFormateada(),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = if (mov.esIngreso) "+${mov.montoCLP} CLP" else "-${mov.montoCLP} CLP",
                                        color = if (mov.esIngreso) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(onClick = { movimientoAEditar = mov }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = Color(0xFF1976D2),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.eliminarMovimiento(mov) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFD32F2F),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoNuevoMovimiento) {
        DialogoNuevoMovimiento(
            alGuardar = { desc, monto, esIngreso ->
                viewModel.registrarMovimientoManual(desc, monto, esIngreso)
                mostrarDialogoNuevoMovimiento = false
            },
            alCancelar = { mostrarDialogoNuevoMovimiento = false }
        )
    }

    movimientoAEditar?.let { mov ->
        DialogoNuevoMovimiento(
            titulo = "Editar Transacción",
            descripcionInicial = mov.descripcion,
            montoInicial = mov.montoCLP.toString(),
            esIngresoInicial = mov.esIngreso,
            alGuardar = { desc, monto, esIngreso ->
                viewModel.actualizarMovimiento(
                    mov.copy(
                        descripcion = desc,
                        montoCLP = monto,
                        esIngreso = esIngreso
                    )
                )
                movimientoAEditar = null
            },
            alCancelar = { movimientoAEditar = null }
        )
    }
}

@Composable
fun DialogoNuevoMovimiento(
    titulo: String = "Registrar Ingreso / Egreso",
    descripcionInicial: String = "",
    montoInicial: String = "",
    esIngresoInicial: Boolean = false,
    alGuardar: (String, Int, Boolean) -> Unit,
    alCancelar: () -> Unit
) {
    var descripcion by remember { mutableStateOf(descripcionInicial) }
    var montoText by remember { mutableStateOf(montoInicial) }
    var esIngreso by remember { mutableStateOf(esIngresoInicial) }

    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = !esIngreso,
                        onClick = { esIngreso = false },
                        label = { Text("Egreso (Gasto)") }
                    )
                    FilterChip(
                        selected = esIngreso,
                        onClick = { esIngreso = true },
                        label = { Text("Ingreso Extra") }
                    )
                }

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (ej. Compra Aceite, Gas)") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = montoText,
                    onValueChange = { montoText = it },
                    label = { Text("Monto en CLP") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val monto = montoText.toIntOrNull() ?: 0
                    if (descripcion.isNotBlank() && monto > 0) {
                        alGuardar(descripcion, monto, esIngreso)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun VistaRecetaChurros(viewModel: DashboardViewModel) {
    val listaReceta by viewModel.listaReceta.collectAsState()
    val listaInsumos by viewModel.listaInsumos.collectAsState()
    var tandasAPreparar by remember { mutableIntStateOf(1) }

    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var ingredienteAEditar by remember { mutableStateOf<RecetaItem?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar a la Receta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(" Configuración de Receta", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Calculadora para ${tandasAPreparar * 40} churros",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Número de tandas (x40 c/u):")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedButton(
                                onClick = { if (tandasAPreparar > 1) tandasAPreparar-- },
                                enabled = tandasAPreparar > 1
                            ) {
                                Text("-")
                            }
                            Text(
                                "$tandasAPreparar",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            OutlinedButton(onClick = { tandasAPreparar++ }) {
                                Text("+")
                            }
                        }
                    }
                }
            }

            Text(" Ingredientes de tu Receta", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            if (listaReceta.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No has registrado ingredientes en tu receta. Presiona '+' para agregar uno.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listaReceta, key = { it.id }) { item ->
                        val cantidadRequerida = item.cantidadParaTanda * tandasAPreparar
                        val insumoEnBodega = listaInsumos.find {
                            it.nombre.contains(item.nombreIngrediente, ignoreCase = true)
                        }
                        val stockSuficiente = (insumoEnBodega?.cantidadDisponible ?: 0.0) >= cantidadRequerida

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF0F8))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.nombreIngrediente, fontWeight = FontWeight.Bold)
                                    Text(
                                        "Requerido: $cantidadRequerida ${item.unidadMedida} (para $tandasAPreparar tanda${if (tandasAPreparar > 1) "s" else ""})",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    if (insumoEnBodega != null) {
                                        Text(
                                            text = if (stockSuficiente) "Stock disponible OK ✅" else "Atención: Falta stock en bodega ⚠️",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (stockSuficiente) Color(0xFF2E7D32) else Color(0xFFC62828)
                                        )
                                    }
                                }

                                Row {
                                    IconButton(onClick = { ingredienteAEditar = item }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2))
                                    }
                                    IconButton(onClick = { viewModel.eliminarIngredienteReceta(item) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoCrear) {
        DialogoIngredienteReceta(
            titulo = "Agregar a la Receta",
            alGuardar = { nombre, cantidad, unidad ->
                viewModel.agregarIngredienteReceta(nombre, cantidad, unidad)
                mostrarDialogoCrear = false
            },
            alCancelar = { mostrarDialogoCrear = false }
        )
    }

    ingredienteAEditar?.let { item ->
        DialogoIngredienteReceta(
            titulo = "Editar Ingrediente de Receta",
            nombreInicial = item.nombreIngrediente,
            cantidadInicial = item.cantidadParaTanda.toString(),
            unidadInicial = item.unidadMedida,
            alGuardar = { nombre, cantidad, unidad ->
                viewModel.actualizarIngredienteReceta(
                    item.copy(
                        nombreIngrediente = nombre,
                        cantidadParaTanda = cantidad,
                        unidadMedida = unidad
                    )
                )
                ingredienteAEditar = null
            },
            alCancelar = { ingredienteAEditar = null }
        )
    }
}

@Composable
fun DialogoIngredienteReceta(
    titulo: String,
    nombreInicial: String = "",
    cantidadInicial: String = "",
    unidadInicial: String = "kg",
    alGuardar: (String, Double, String) -> Unit,
    alCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var cantidadText by remember { mutableStateOf(cantidadInicial) }
    var unidad by remember { mutableStateOf(unidadInicial) }

    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Ingrediente (ej. Harina, Aceite)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = cantidadText,
                    onValueChange = { cantidadText = it },
                    label = { Text("Cantidad por Tanda (40 churros)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad de Medida (kg, L, g)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cant = cantidadText.toDoubleOrNull() ?: 0.0
                    if (nombre.isNotBlank() && cant > 0) {
                        alGuardar(nombre, cant, unidad)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar")
            }
        }
    )
}