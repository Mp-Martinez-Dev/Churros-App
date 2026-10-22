package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receta_items")
data class RecetaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreIngrediente: String,
    val cantidadParaTanda: Double, // Cantidad requerida para 1 tanda (40 churros)
    val unidadMedida: String
)