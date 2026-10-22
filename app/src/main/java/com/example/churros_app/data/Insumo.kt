package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insumos")
data class Insumo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,              // Ej: "Harina", "Levadura", "Aceite"
    val unidadMedida: String,        // Ej: "gr", "ml", "unidades"
    val cantidadDisponible: Double,  // Ej: 1000.0
    val costoPorUnidadCLP: Double    // Para calcular el costo exacto por receta
)