package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,              // Ej: "Palito de Churro"
    val precioVentaCLP: Int,         // Ej: 200
    val rendimientoPorTanda: Int     // Ej: 40 palitos por tanda
)