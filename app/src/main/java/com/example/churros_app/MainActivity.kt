package com.example.churros_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.churros_app.data.AppDatabase
import com.example.churros_app.ui.DashboardScreen
import com.example.churros_app.ui.DashboardViewModel
import com.example.churros_app.ui.theme.ChurrosAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializamos la base de datos local Room
        val baseDatos = AppDatabase.obtenerBaseDatos(applicationContext)
        val productoDao = baseDatos.productoDao()

        // 2. Creador para inyectar el DAO en el ViewModel
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return DashboardViewModel(productoDao) as T
                }
                throw IllegalArgumentException("ViewModel no reconocido")
            }
        }

        setContent {
            ChurrosAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dashboardViewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
                    DashboardScreen(viewModel = dashboardViewModel)
                }
            }
        }
    }
}