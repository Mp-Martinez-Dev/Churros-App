package com.example.churros_app.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.churros_app.R
import com.example.churros_app.data.ComboEntity
import com.example.churros_app.data.Insumo
import com.example.churros_app.data.MisionDiaria
import com.example.churros_app.data.Movimiento
import com.example.churros_app.data.RecetaItem

// ==========================================
// PALETA DE COLORES PASTELERÍA
// ==========================================
val ColorChocolate = Color(0xFF3E2723)
val ColorSecundario = Color(0xFF6D4C41)
val ColorCanela = Color(0xFF8D6E63)
val ColorCaramelo = Color(0xFFD84315)
val ColorCremaFondo = Color(0xFFFAF5EF)
val ColorCremaCard = Color(0xFFFFFDF9)

// ==========================================
// COMPONENTES REUTILIZABLES
// ==========================================

@Composable
fun FondoPasteleria(
    modifier: Modifier = Modifier,
    alphaLogo: Float = 0.10f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ColorCremaFondo)
    ) {
        // Marca de agua transparente centrada
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.Center)
                .graphicsLayer(alpha = alphaLogo)
        )
        content()
    }
}

@Composable
fun TarjetaPasteleria(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ColorCremaCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun BotonPasteleria(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtexto: String? = null,
    enabled: Boolean = true,
    containerColor: Color = ColorCanela
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = Color(0xFFE0E0E0)
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = texto, fontWeight = FontWeight.Bold)
            if (!subtexto.isNullOrBlank()) {
                Text(text = subtexto, fontSize = 11.sp, color = Color(0xFFFFF8E1))
            }
        }
    }
}

@Composable
fun BotonSecundarioPasteleria(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtexto: String? = null,
    enabled: Boolean = true,
    colorBorde: Color = ColorCanela,
    colorTexto: Color = ColorChocolate
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = texto, fontWeight = FontWeight.Bold, color = colorTexto)
            if (!subtexto.isNullOrBlank()) {
                Text(text = subtexto, fontSize = 11.sp, color = ColorCaramelo)
            }
        }
    }
}

// ==========================================
// PANTALLA PRINCIPAL DASHBOARD
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    var pestañaSeleccionada by remember { mutableIntStateOf(0) }
    val mision by viewModel.misionActual.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val imagenRes = when (pestañaSeleccionada) {
                        0 -> R.drawable.ic_cerdito
                        1 -> R.drawable.ic_bodega
                        2 -> R.drawable.ic_historial
                        else -> R.drawable.ic_receta
                    }

                    val textoTitulo = when (pestañaSeleccionada) {
                        0 -> "Alcancía Diaria"
                        1 -> "Bodega e Insumos"
                        2 -> "Historial de Ventas"
                        else -> "Receta & Costos"
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = ColorCanela.copy(alpha = 0.3f),
                            shape = CircleShape,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = imagenRes),
                                    contentDescription = textoTitulo,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Text(
                            text = textoTitulo,
                            color = ColorCremaCard,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorChocolate)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = ColorCremaCard) {
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
        FondoPasteleria(modifier = Modifier.padding(paddingValues)) {
            when (pestañaSeleccionada) {
                0 -> VistaAlcancíaDiaria(mision = mision, viewModel = viewModel)
                1 -> VistaBodegaInsumos(viewModel = viewModel)
                2 -> VistaHistorialMovimientos(viewModel = viewModel)
                3 -> VistaRecetaChurros(viewModel = viewModel)
            }
        }
    }
}

// ==========================================
// ANIMACIÓN Y VISTA ALCANCÍA / COMBOS
// ==========================================

@Composable
fun AnimacionCaidaMoneda(
    ejecutar: Boolean,
    alFinalizar: () -> Unit
) {
    if (!ejecutar) return

    val animacionOffsetY = remember { Animatable(0f) }
    val animacionAlpha = remember { Animatable(1f) }

    LaunchedEffect(ejecutar) {
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
            .background(Color(0xFFFFD700), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$", color = ColorChocolate, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
@Composable
fun VistaAlcancíaDiaria(mision: MisionDiaria?, viewModel: DashboardViewModel) {
    var animarMoneda by remember { mutableStateOf(false) }

    // Estados para los diálogos CRUD
    var mostrarGestorCombos by remember { mutableStateOf(false) }
    var comboAEditar by remember { mutableStateOf<ComboEntity?>(null) }
    var mostrarFormularioCombo by remember { mutableStateOf(false) }

    // Lectura reactiva desde Room
    val listaCombos by viewModel.listaCombos.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            mision?.let { m ->
                val churrosDisponibles = (m.metaUnidades - m.unidadesVendidas).coerceAtLeast(0)
                val porcentaje = if (m.metaUnidades > 0) {
                    ((m.unidadesVendidas.toFloat() / m.metaUnidades.toFloat()) * 100).coerceAtMost(100f)
                } else 0f
                val metaCLP = m.metaUnidades * 200

                val colorTargetPildora = when {
                    porcentaje >= 100f -> Color(0xFF4CAF50)
                    porcentaje >= 80f  -> Color(0xFF29B6F6)
                    porcentaje >= 50f  -> Color(0xFFFF9800)
                    else               -> Color(0xFFE57373)
                }

                val colorPildoraAnimado by animateColorAsState(
                    targetValue = colorTargetPildora,
                    animationSpec = tween(durationMillis = 500),
                    label = "ColorPildoraProgresiva"
                )

                // 1. TARJETA PRINCIPAL: MISIÓN Y ALCANCÍA
                TarjetaPasteleria {
                    Text(
                        text = " Misión del Día ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = ColorChocolate,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Box(
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            AnimacionCaidaMoneda(
                                ejecutar = animarMoneda,
                                alFinalizar = { animarMoneda = false }
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.cerdito),
                            contentDescription = "Alcancía Cerdito",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }

                    Surface(
                        color = colorPildoraAnimado,
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${porcentaje.toInt()}% Cumplido",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(text = "•", color = Color.White.copy(alpha = 0.8f))
                            Text(
                                text = "${m.ingresoAcumuladoCLP} CLP",
                                color = Color(0xFFFFF59D),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Progreso de Venta", fontSize = 12.sp, color = ColorSecundario)
                            Text("${m.unidadesVendidas} de ${m.metaUnidades} churros", fontWeight = FontWeight.Bold, color = ColorChocolate)
                            Text(
                                "Quedan: $churrosDisponibles churros",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (churrosDisponibles == 0) Color(0xFFC62828) else Color(0xFF388E3C)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Meta Objetiva", fontSize = 12.sp, color = ColorSecundario)
                            Text("$metaCLP CLP", fontWeight = FontWeight.Bold, color = ColorCaramelo)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. REGISTRO RÁPIDO Y COMBOS CON BOTONES SUPERIORES JUNTO AL TÍTULO
                TarjetaPasteleria {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " Venta Rápida & Combos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ColorChocolate
                        )

                        // ROW DE ACCIONES: BOTÓN DE AJUSTES Y BOTÓN AGREGAR (+) JUNTO AL TÍTULO
                        Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                            IconButton(onClick = { mostrarGestorCombos = true }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Administrar Combos",
                                    tint = ColorCanela
                                )
                            }
                            IconButton(onClick = {
                                comboAEditar = null
                                mostrarFormularioCombo = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Nuevo Combo",
                                    tint = ColorCanela
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // GRILLA DE BOTONES UNIFICADOS (+1, +5 Y COMBOS PERSONALIZADOS)
                    val listaOpcionesBase = listOf(
                        Pair("+1 Churro", 1 to 200),
                        Pair("+5 Churros", 5 to 1000)
                    )

                    val listaTodosLosBotones = listaOpcionesBase.map { (titulo, datos) ->
                        Triple(titulo, datos.first, datos.second)
                    } + listaCombos.map { combo ->
                        Triple("${combo.nombre} (${combo.cantidadChurros}u)", combo.cantidadChurros, combo.precioCLP)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        listaTodosLosBotones.chunked(2).forEach { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                fila.forEach { (nombre, cantidad, precio) ->
                                    BotonPasteleria(
                                        texto = nombre,
                                        subtexto = "$$precio CLP",
                                        onClick = {
                                            viewModel.registrarVenta(cantidad, precioPersonalizadoCLP = precio)
                                            animarMoneda = true
                                        },
                                        enabled = churrosDisponibles >= cantidad,
                                        containerColor = ColorCanela,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (fila.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEFEBE9))

                    // BOTÓN INFERIOR RESTAURADO A PREPARAR NUEVA TANDA
                    BotonSecundarioPasteleria(
                        texto = "+ Preparar Nueva Tanda (+40 churros)",
                        onClick = { viewModel.prepararNuevaTanda() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // ==========================================
    // DIÁLOGO CRUD: ADMINISTRAR COMBOS
    // ==========================================
    if (mostrarGestorCombos) {
        AlertDialog(
            onDismissRequest = { mostrarGestorCombos = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Combos Registrados", fontWeight = FontWeight.Bold, color = ColorChocolate)
                    IconButton(onClick = {
                        comboAEditar = null
                        mostrarFormularioCombo = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Combo", tint = ColorCanela)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (listaCombos.isEmpty()) {
                        Text(
                            "No hay combos personalizados creados. Toca '+' para agregar uno.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.heightIn(max = 280.dp)
                        ) {
                            items(listaCombos, key = { it.id }) { combo ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = ColorCremaFondo),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(combo.nombre, fontWeight = FontWeight.Bold, color = ColorChocolate)
                                            Text("${combo.cantidadChurros} churros • $${combo.precioCLP} CLP", fontSize = 12.sp, color = ColorSecundario)
                                        }

                                        Row {
                                            IconButton(onClick = {
                                                comboAEditar = combo
                                                mostrarFormularioCombo = true
                                            }) {
                                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = ColorCanela)
                                            }
                                            IconButton(onClick = { viewModel.eliminarCombo(combo) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarGestorCombos = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorCanela)
                ) {
                    Text("Cerrar")
                }
            }
        )
    }

    // ==========================================
    // DIÁLOGO FORMULARIO: CREAR / EDITAR COMBO CON VALIDACIONES
    // ==========================================
    if (mostrarFormularioCombo) {
        var nombreCombo by remember { mutableStateOf(comboAEditar?.nombre ?: "") }
        var cantidadChurrosText by remember { mutableStateOf(comboAEditar?.cantidadChurros?.toString() ?: "") }
        var precioComboText by remember { mutableStateOf(comboAEditar?.precioCLP?.toString() ?: "") }

        // Control para activar errores solo al presionar "Guardar"
        var intentadoGuardar by remember { mutableStateOf(false) }

        // Criterios de validación
        val esNombreInvalido = intentadoGuardar && nombreCombo.isBlank()
        val cantInt = cantidadChurrosText.toIntOrNull() ?: 0
        val esCantidadInvalida = intentadoGuardar && cantInt <= 0
        val precioInt = precioComboText.toIntOrNull()
        val esPrecioInvalido = intentadoGuardar && (precioInt == null || precioInt <= 0)

        val precioBaseSugerido = cantInt * 200

        AlertDialog(
            onDismissRequest = {
                mostrarFormularioCombo = false
                comboAEditar = null
            },
            title = {
                Text(
                    text = if (comboAEditar != null) "Editar Combo" else "Nuevo Combo",
                    fontWeight = FontWeight.Bold,
                    color = ColorChocolate
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // 1. CAMPO NOMBRE
                    Column {
                        OutlinedTextField(
                            value = nombreCombo,
                            onValueChange = { nombreCombo = it },
                            label = { Text("Nombre del Combo (ej. Pack Familiar)") },
                            singleLine = true,
                            isError = esNombreInvalido,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (esNombreInvalido) {
                            Text(
                                text = "⚠️ El nombre no puede estar vacío",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // 2. CAMPO CANTIDAD DE CHURROS
                    Column {
                        OutlinedTextField(
                            value = cantidadChurrosText,
                            onValueChange = { cantidadChurrosText = it.filter { char -> char.isDigit() } },
                            label = { Text("Cantidad de Churros") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            isError = esCantidadInvalida,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (esCantidadInvalida) {
                            Text(
                                text = " Ingrese una cantidad mayor a 0",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }

                    // VALOR BASE SUGERIDO
                    if (cantInt > 0) {
                        Text(
                            text = " Valor base estándar (x$cantInt): $$precioBaseSugerido CLP",
                            fontSize = 12.sp,
                            color = ColorSecundario,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // 3. CAMPO PRECIO FINAL
                    Column {
                        OutlinedTextField(
                            value = precioComboText,
                            onValueChange = { precioComboText = it.filter { char -> char.isDigit() } },
                            label = { Text("Precio Final del Combo (CLP)") },
                            placeholder = { if (precioBaseSugerido > 0) Text("Ej. $precioBaseSugerido") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            isError = esPrecioInvalido,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (esPrecioInvalido) {
                            Text(
                                text = " Ingrese un precio válido en CLP",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        intentadoGuardar = true
                        val prec = precioComboText.toIntOrNull() ?: precioBaseSugerido

                        if (nombreCombo.isNotBlank() && cantInt > 0 && prec > 0) {
                            if (comboAEditar != null) {
                                // EDICIÓN
                                viewModel.actualizarCombo(
                                    comboAEditar!!.copy(
                                        nombre = nombreCombo.trim(),
                                        cantidadChurros = cantInt,
                                        precioCLP = prec
                                    )
                                )
                            } else {
                                // CREACIÓN
                                viewModel.agregarCombo(
                                    nombre = nombreCombo.trim(),
                                    cantidadChurros = cantInt,
                                    precioCLP = prec
                                )
                            }
                            mostrarFormularioCombo = false
                            comboAEditar = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorCanela)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarFormularioCombo = false
                        comboAEditar = null
                    }
                ) {
                    Text("Cancelar", color = ColorSecundario)
                }
            }
        )
    }
}
// ==========================================
// VISTAS SECUNDARIAS (BODEGA, HISTORIAL, RECETA)
// ==========================================
@Composable
fun VistaBodegaInsumos(viewModel: DashboardViewModel) {
    val listaInsumos by viewModel.listaInsumos.collectAsState()
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var insumoAEditar by remember { mutableStateOf<Insumo?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = ColorCanela,
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
            Text(
                text = "Inventario de Bodega",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorChocolate
            )

            if (listaInsumos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay insumos registrados. Haz clic en '+' para agregar uno.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = listaInsumos,
                        key = { insumo: Insumo -> insumo.id }
                    ) { insumo: Insumo ->
                        TarjetaPasteleria {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = insumo.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = ColorChocolate
                                    )
                                    Text(
                                        text = "${insumo.cantidadDisponible} ${insumo.unidadMedida}",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Row {
                                    IconButton(onClick = { insumoAEditar = insumo }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = ColorCanela
                                        )
                                    }
                                    IconButton(onClick = { viewModel.eliminarInsumo(insumo) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFD32F2F)
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
                viewModel.actualizarInsumo(
                    insumo.copy(
                        nombre = nombre,
                        cantidadDisponible = cantidad,
                        unidadMedida = unidad
                    )
                )
                insumoAEditar = null
            },
            alCancelar = { insumoAEditar = null }
        )
    }
}

// ==========================================
// DIÁLOGO REUTILIZABLE: INSUMO CON VALIDACIONES Y ERRORES VISUALES
// ==========================================
@Composable
fun DialogoInsumo(
    titulo: String,
    nombreInicial: String = "",
    cantidadInicial: String = "",
    unidadInicial: String = "kg",
    alGuardar: (nombre: String, cantidad: Double, unidad: String) -> Unit,
    alCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var cantidadText by remember { mutableStateOf(cantidadInicial) }
    var unidad by remember { mutableStateOf(unidadInicial) }

    // Control para mostrar errores solo tras presionar "Guardar"
    var intentadoGuardar by remember { mutableStateOf(false) }

    // Reglas de validación
    val esNombreInvalido = intentadoGuardar && nombre.isBlank()
    val cantidadDouble = cantidadText.toDoubleOrNull()
    val esCantidadInvalida = intentadoGuardar && (cantidadDouble == null || cantidadDouble <= 0.0)
    val esUnidadInvalida = intentadoGuardar && unidad.isBlank()

    AlertDialog(
        onDismissRequest = alCancelar,
        title = {
            Text(text = titulo, fontWeight = FontWeight.Bold, color = ColorChocolate)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. CAMPO NOMBRE
                Column {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Insumo (ej. Harina, Aceite)") },
                        singleLine = true,
                        isError = esNombreInvalido,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esNombreInvalido) {
                        Text(
                            text = "⚠️ El nombre no puede estar vacío",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // 2. CAMPO CANTIDAD DISPONIBLE
                Column {
                    OutlinedTextField(
                        value = cantidadText,
                        onValueChange = { texto ->
                            // Permitir números y un solo punto o coma decimal
                            if (texto.isEmpty() || texto.matches(Regex("^\\d*([.,]?\\d*)?$"))) {
                                cantidadText = texto.replace(',', '.')
                            }
                        },
                        label = { Text("Cantidad Disponible") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = esCantidadInvalida,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esCantidadInvalida) {
                        Text(
                            text = "⚠️ Ingrese una cantidad mayor a 0",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // 3. CAMPO UNIDAD DE MEDIDA
                Column {
                    OutlinedTextField(
                        value = unidad,
                        onValueChange = { unidad = it },
                        label = { Text("Unidad de Medida (ej. kg, g, l, ml)") },
                        singleLine = true,
                        isError = esUnidadInvalida,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esUnidadInvalida) {
                        Text(
                            text = "⚠️ Especifique la unidad de medida",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    intentadoGuardar = true
                    if (nombre.isNotBlank() && cantidadDouble != null && cantidadDouble > 0.0 && unidad.isNotBlank()) {
                        alGuardar(nombre.trim(), cantidadDouble, unidad.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorCanela)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar", color = ColorSecundario)
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
    val totalTransaccionado = (totalIngresos + totalEgresos).coerceAtLeast(1)

    // Porcentajes para el gráfico de barra proporcional
    val pctIngresos = (totalIngresos.toFloat() / totalTransaccionado.toFloat())
    val pctEgresos = (totalEgresos.toFloat() / totalTransaccionado.toFloat())

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoNuevoMovimiento = true },
                containerColor = ColorCanela,
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
            Text(
                text = "Resumen del Negocio",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorChocolate
            )

            // TARJETA DE RESUMEN CON GRÁFICO BARRAS DE PROPORCIÓN
            TarjetaPasteleria {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Ingresos", fontSize = 12.sp, color = Color.Gray)
                            Text("+$totalIngresos CLP", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Egresos", fontSize = 12.sp, color = Color.Gray)
                            Text("-$totalEgresos CLP", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Balance Total", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "$balanceNeto CLP",
                                color = if (balanceNeto >= 0) Color(0xFF388E3C) else Color(0xFFC62828),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // --- GRÁFICO VISUAL DE BARRAS DE PROPORCIÓN ---
                    if (listaMovimientos.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Distribución de Flujo de Caja",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorSecundario
                            )

                            // Barra de progreso dividida (Verde = Ingresos, Rojo = Egresos)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(14.dp)
                                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(7.dp))
                            ) {
                                if (pctIngresos > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(pctIngresos)
                                            .background(
                                                Color(0xFF4CAF50),
                                                shape = if (pctEgresos == 0f) RoundedCornerShape(7.dp) else RoundedCornerShape(topStart = 7.dp, bottomStart = 7.dp)
                                            )
                                    )
                                }
                                if (pctEgresos > 0f) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(pctEgresos)
                                            .background(
                                                Color(0xFFE57373),
                                                shape = if (pctIngresos == 0f) RoundedCornerShape(7.dp) else RoundedCornerShape(topEnd = 7.dp, bottomEnd = 7.dp)
                                            )
                                    )
                                }
                            }

                            // Leyenda del Gráfico
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ingresos (${(pctIngresos * 100).toInt()}%)",
                                    fontSize = 10.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Egresos (${(pctEgresos * 100).toInt()}%)",
                                    fontSize = 10.sp,
                                    color = Color(0xFFC62828),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "Historial de Transacciones",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = ColorChocolate
            )

            // LISTA DE MOVIMIENTOS CON EDICIÓN Y ELIMINACIÓN
            if (listaMovimientos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay movimientos registrados. Toca '+' para agregar uno.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = listaMovimientos,
                        key = { mov: Movimiento -> mov.id }
                    ) { mov: Movimiento ->
                        TarjetaPasteleria {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mov.descripcion,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorChocolate
                                    )
                                    Text(
                                        text = mov.fechaHora,
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "${if (mov.esIngreso) "+" else "-"}${mov.montoCLP} CLP",
                                        color = if (mov.esIngreso) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(onClick = { movimientoAEditar = mov }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar Movimiento",
                                            tint = ColorCanela,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(onClick = { viewModel.eliminarMovimiento(mov) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar Movimiento",
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

    // DIÁLOGO PARA REGISTRAR NUEVO MOVIMIENTO
    if (mostrarDialogoNuevoMovimiento) {
        DialogoNuevoMovimiento(
            alGuardar = { desc, monto, esIngreso ->
                viewModel.registrarMovimientoManual(desc, monto, esIngreso)
                mostrarDialogoNuevoMovimiento = false
            },
            alCancelar = { mostrarDialogoNuevoMovimiento = false }
        )
    }

    // DIÁLOGO PARA EDITAR UN MOVIMIENTO EXISTENTE
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

// ==========================================
// DIÁLOGO REUTILIZABLE: MOVIMIENTO CON VALIDACIONES
// ==========================================
@Composable
fun DialogoNuevoMovimiento(
    titulo: String = "Nuevo Movimiento Manual",
    descripcionInicial: String = "",
    montoInicial: String = "",
    esIngresoInicial: Boolean = false,
    alGuardar: (descripcion: String, montoCLP: Int, esIngreso: Boolean) -> Unit,
    alCancelar: () -> Unit
) {
    var descripcion by remember { mutableStateOf(descripcionInicial) }
    var montoText by remember { mutableStateOf(montoInicial) }
    var esIngreso by remember { mutableStateOf(esIngresoInicial) }

    // Control para mostrar errores solo tras presionar Guardar
    var intentadoGuardar by remember { mutableStateOf(false) }

    // Criterios de validación
    val esDescripcionInvalida = intentadoGuardar && descripcion.isBlank()
    val montoInt = montoText.toIntOrNull()
    val esMontoInvalido = intentadoGuardar && (montoInt == null || montoInt <= 0)

    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text(text = titulo, fontWeight = FontWeight.Bold, color = ColorChocolate) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // SELECTOR TIPO: INGRESO O EGRESO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !esIngreso,
                        onClick = { esIngreso = false },
                        label = { Text("Egreso / Gasto") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE57373),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = esIngreso,
                        onClick = { esIngreso = true },
                        label = { Text("Ingreso Extra") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF81C784),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // 1. CAMPO DESCRIPCIÓN
                Column {
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción (ej. Arriendo, Venta evento)") },
                        singleLine = true,
                        isError = esDescripcionInvalida,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esDescripcionInvalida) {
                        Text(
                            text = "⚠️ La descripción no puede estar vacía",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // 2. CAMPO MONTO CLP
                Column {
                    OutlinedTextField(
                        value = montoText,
                        onValueChange = { texto ->
                            // Filtrar solo valores numéricos
                            montoText = texto.filter { char -> char.isDigit() }
                        },
                        label = { Text("Monto (CLP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = esMontoInvalido,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esMontoInvalido) {
                        Text(
                            text = " Ingrese un monto válido mayor a 0 CLP",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    intentadoGuardar = true
                    if (descripcion.isNotBlank() && montoInt != null && montoInt > 0) {
                        alGuardar(descripcion.trim(), montoInt, esIngreso)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorCanela)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar", color = ColorSecundario)
            }
        }
    )
}@Composable
fun VistaRecetaChurros(viewModel: DashboardViewModel) {
    val listaReceta by viewModel.listaReceta.collectAsState()
    val listaInsumos by viewModel.listaInsumos.collectAsState()
    var tandasAPreparar by remember { mutableIntStateOf(1) }

    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var ingredienteAEditar by remember { mutableStateOf<RecetaItem?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarDialogoCrear = true },
                containerColor = ColorCanela,
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
            Text(
                text = "Configuración de Receta Base",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorChocolate
            )

            // CALCULADORA DE TANDAS
            TarjetaPasteleria {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Calculadora para ${tandasAPreparar * 40} churros",
                        fontWeight = FontWeight.Bold,
                        color = ColorChocolate
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { if (tandasAPreparar > 1) tandasAPreparar-- },
                            enabled = tandasAPreparar > 1,
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }

                        Text(
                            text = "$tandasAPreparar",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = ColorChocolate,
                            fontSize = 16.sp
                        )

                        OutlinedButton(
                            onClick = { tandasAPreparar++ },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape
                        ) {
                            Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }

            Text(
                text = "Ingredientes de tu Receta Base",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = ColorChocolate
            )

            // LISTA DE INGREDIENTES CON ACCIONES CRUD
            if (listaReceta.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No has registrado ingredientes. Haz clic en '+' para agregar uno.",
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(
                        items = listaReceta,
                        key = { item: RecetaItem -> item.id }
                    ) { item: RecetaItem ->
                        // Cálculo correcto de cantidad requerida según tandas
                        val cantidadRequerida = item.cantidadParaTanda * tandasAPreparar

                        val insumoEnBodega = listaInsumos.find {
                            it.nombre.contains(item.nombreIngrediente, ignoreCase = true)
                        }

                        // Verificación inteligente comparando en unidad base (g / ml)
                        val stockSuficiente = if (insumoEnBodega != null) {
                            val requeridoBase = viewModel.convertirACantidadBase(cantidadRequerida, item.unidadMedida)
                            val stockBodegaBase = viewModel.convertirACantidadBase(insumoEnBodega.cantidadDisponible, insumoEnBodega.unidadMedida)
                            stockBodegaBase >= requeridoBase
                        } else {
                            false
                        }

                        TarjetaPasteleria {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.nombreIngrediente,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorChocolate
                                    )
                                    Text(
                                        text = "Requerido: $cantidadRequerida ${item.unidadMedida} ($tandasAPreparar tanda${if (tandasAPreparar > 1) "s" else ""})",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    if (insumoEnBodega != null) {
                                        Text(
                                            text = if (stockSuficiente) "Stock disponible OK " else "Atención: Falta stock en bodega ",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (stockSuficiente) Color(0xFF2E7D32) else Color(0xFFC62828)
                                        )
                                    } else {
                                        Text(
                                            text = "No registrado en bodega ",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Row {
                                    IconButton(onClick = { ingredienteAEditar = item }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = ColorCanela
                                        )
                                    }
                                    IconButton(onClick = { viewModel.eliminarIngredienteReceta(item) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFD32F2F)
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

    // DIÁLOGO CREAR INGREDIENTE
    if (mostrarDialogoCrear) {
        DialogoIngredienteReceta(
            titulo = "Agregar Ingrediente a Receta",
            alGuardar = { nombre, cantidad, unidad ->
                viewModel.agregarIngredienteReceta(nombre, cantidad, unidad)
                mostrarDialogoCrear = false
            },
            alCancelar = { mostrarDialogoCrear = false }
        )
    }

    // DIÁLOGO EDITAR INGREDIENTE
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

// ==========================================
// DIÁLOGO REUTILIZABLE: INGREDIENTE CON VALIDACIONES
// ==========================================
@Composable
fun DialogoIngredienteReceta(
    titulo: String,
    nombreInicial: String = "",
    cantidadInicial: String = "",
    unidadInicial: String = "g",
    alGuardar: (nombre: String, cantidad: Double, unidad: String) -> Unit,
    alCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var cantidadText by remember { mutableStateOf(cantidadInicial) }
    var unidad by remember { mutableStateOf(unidadInicial) }

    // Control para mostrar errores solo tras intentar guardar
    var intentadoGuardar by remember { mutableStateOf(false) }

    // Reglas de validación
    val esNombreInvalido = intentadoGuardar && nombre.isBlank()
    val cantidadDouble = cantidadText.toDoubleOrNull()
    val esCantidadInvalida = intentadoGuardar && (cantidadDouble == null || cantidadDouble <= 0.0)
    val esUnidadInvalida = intentadoGuardar && unidad.isBlank()

    AlertDialog(
        onDismissRequest = alCancelar,
        title = {
            Text(text = titulo, fontWeight = FontWeight.Bold, color = ColorChocolate)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // 1. CAMPO NOMBRE DEL INGREDIENTE
                Column {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Ingrediente (ej. Harina, Agua, Sal)") },
                        singleLine = true,
                        isError = esNombreInvalido,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esNombreInvalido) {
                        Text(
                            text = " Ingrese el nombre del ingrediente",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // 2. CAMPO CANTIDAD PARA 1 TANDA
                Column {
                    OutlinedTextField(
                        value = cantidadText,
                        onValueChange = { texto ->
                            if (texto.isEmpty() || texto.matches(Regex("^\\d*([.,]?\\d*)?$"))) {
                                cantidadText = texto.replace(',', '.')
                            }
                        },
                        label = { Text("Cantidad por Tanda (40 churros)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = esCantidadInvalida,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esCantidadInvalida) {
                        Text(
                            text = "⚠️ Ingrese una cantidad válida mayor a 0",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }

                // 3. CAMPO UNIDAD DE MEDIDA
                Column {
                    OutlinedTextField(
                        value = unidad,
                        onValueChange = { unidad = it },
                        label = { Text("Unidad (ej. g, ml, kg, cda)") },
                        singleLine = true,
                        isError = esUnidadInvalida,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (esUnidadInvalida) {
                        Text(
                            text = " Ingrese la unidad de medida",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    intentadoGuardar = true
                    if (nombre.isNotBlank() && cantidadDouble != null && cantidadDouble > 0.0 && unidad.isNotBlank()) {
                        alGuardar(nombre.trim(), cantidadDouble, unidad.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorCanela)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar", color = ColorSecundario)
            }
        }
    )

}