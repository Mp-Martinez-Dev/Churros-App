package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "combos")
data class ComboEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cantidadChurros: Int,
    val precioCLP: Int
)