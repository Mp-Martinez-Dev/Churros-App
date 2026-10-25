package com.example.churros_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.churros_app.R

@Composable
fun FondoPasteleria(
    modifier: Modifier = Modifier,
    alphaLogo: Float = 0.10f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EF)) // Fondo Crema Pastel
    ) {
        // Marca de agua transparente
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.Center)
                .graphicsLayer(alpha = alphaLogo)
        )

        // Contenido de la pantalla
        content()
    }
}