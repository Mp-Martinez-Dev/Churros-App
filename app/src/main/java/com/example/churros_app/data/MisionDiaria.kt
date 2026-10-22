package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mision_diaria")
data class MisionDiaria(
    @PrimaryKey
    val fechaCodigo: String, // Formato "YYYYMMDD"
    val metaUnidades: Int,
    val unidadesVendidas: Int,
    val ingresoAcumuladoCLP: Int
)