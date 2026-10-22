package com.example.churros_app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlcanciaCerdito(
    montoActualCLP: Int,
    metaMontoCLP: Int,
    unidadesVendidas: Int,
    metaUnidades: Int,
    modifier: Modifier = Modifier
) {
    // Calculamos el porcentaje de llenado (de 0.0 a 1.0)
    val porcentajeLlenado = if (metaMontoCLP > 0) {
        (montoActualCLP.toFloat() / metaMontoCLP.toFloat()).coerceIn(0f, 1f)
    } else 0f

    // Animación suave del nivel de la alcancía
    val progresoAnimado by animateFloatAsState(
        targetValue = porcentajeLlenado,
        animationSpec = tween(durationMillis = 800),
        label = "animacion_alcancia"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🐷 Misión del Día",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ilustración/Contenedor de la Alcancía
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFD1DC)) // Rosa claro base
                    .border(4.dp, Color(0xFFFF8DA1), CircleShape),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Nivel de llenado de monedas (Rosa intenso animado)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(progresoAnimado)
                        .background(Color(0xFFFF5277))
                )

                // Contenido central dentro del cerdito
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${(porcentajeLlenado * 100).toInt()}%",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (progresoAnimado > 0.5f) Color.White else Color(0xFF8B0032)
                    )
                    Text(
                        text = "$montoActualCLP CLP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (progresoAnimado > 0.5f) Color.White else Color(0xFF8B0032)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Indicador numérico de Meta y Unidades
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Progreso de Venta",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "$unidadesVendidas de $metaUnidades churros",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Meta Objetiva",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "$metaMontoCLP CLP",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}