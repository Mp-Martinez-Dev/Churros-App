package com.example.churros_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores base de la paleta pastelería
val ColorCanela = Color(0xFF8D6E63)
val ColorChocolate = Color(0xFF3E2723)
val ColorCaramelo = Color(0xFFD84315)

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