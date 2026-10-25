package com.example.churros_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores Neón & Glasscorp
val ColorNeonVerde = Color(0xFF78FFD6)
val ColorTextoVidrio = Color(0xFFF0F4F8)
val ColorTextoSecundario = Color(0xFF9AA5B1)

@Composable
fun BotonPasteleria(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtexto: String? = null,
    enabled: Boolean = true,
    containerColor: Color = Color(0x2BFFFFFF) // Fondo translúcido por defecto
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0x40FFFFFF)),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            disabledContainerColor = Color(0x1AFFFFFF)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                color = ColorTextoVidrio
            )
            if (!subtexto.isNullOrBlank()) {
                Text(
                    text = subtexto,
                    fontSize = 11.sp,
                    color = ColorNeonVerde,
                    fontWeight = FontWeight.Medium
                )
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
    colorBorde: Color = Color(0x66FFFFFF),
    colorTexto: Color = ColorTextoVidrio
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            if (!subtexto.isNullOrBlank()) {
                Text(
                    text = subtexto,
                    fontSize = 11.sp,
                    color = ColorNeonVerde
                )
            }
        }
    }
}