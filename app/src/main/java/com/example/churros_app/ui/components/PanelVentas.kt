package com.example.churros_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PanelVentas(
    precioChurroCLP: Int = 250,
    alVenderChurros: (cantidad: Int) -> Unit,
    alAgregarTanda: () -> Unit,
    modifier: Modifier = Modifier
) {
    TarjetaPasteleria(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡ Venta Rápida",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ColorTextoVidrio
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Botones de venta rápida estilo cristal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BotonPasteleria(
                    texto = "+1 Churro",
                    subtexto = "$${precioChurroCLP} CLP",
                    onClick = { alVenderChurros(1) },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                )

                BotonPasteleria(
                    texto = "+5 Churros",
                    subtexto = "$${precioChurroCLP * 5} CLP",
                    onClick = { alVenderChurros(5) },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    containerColor = Color(0x3378FFD6) // Fondo Neón sutil
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = Color(0x26FFFFFF))

            Spacer(modifier = Modifier.height(14.dp))

            // Botón de Preparar Nueva Tanda
            BotonSecundarioPasteleria(
                texto = "+ Preparar Nueva Tanda (+40 churros)",
                onClick = alAgregarTanda,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colorBorde = Color(0x8078FFD6),
                colorTexto = ColorNeonVerde
            )
        }
    }
}