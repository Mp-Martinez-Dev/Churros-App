package com.example.churros_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Degradado iridiscente para bordes estilo cristal Glasscorp
val BordeGlassGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x80A8FF78), // Verde Neón iridiscente
        Color(0x8078FFD6), // Turquesa Neón
        Color(0x80FF78C4), // Rosa translúcido
        Color(0x30FFFFFF)  // Brillo blanco
    )
)

@Composable
fun TarjetaPasteleria(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color(0x24FFFFFF)) // Transparencia oscura
            .border(
                width = 1.dp,
                brush = BordeGlassGradient,
                shape = forma
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}