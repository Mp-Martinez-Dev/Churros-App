package com.example.churros_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descripcion: String,
    val montoCLP: Int,
    val esIngreso: Boolean,
    val fechaHora: String // Guarda la fecha/hora en formato String "dd/MM/yyyy HH:mm"
) {
    fun obtenerFechaFormateada(): String {
        return try {
            val entradaFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = entradaFormat.parse(fechaHora) ?: Date()
            val salidaFormat = SimpleDateFormat("HH:mm 'hrs' / dd/MM/yyyy", Locale.getDefault())
            salidaFormat.format(date)
        } catch (e: Exception) {
            fechaHora // Devuelve el string original si no se puede formatear
        }
    }
}