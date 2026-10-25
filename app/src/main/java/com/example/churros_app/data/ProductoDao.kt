package com.example.churros_app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    // --- MÉTODOS PARA COMBOS ---
    @Query("SELECT * FROM combos ORDER BY id ASC")
    fun obtenerTodosLosCombos(): Flow<List<Combo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCombo(combo: Combo)

    @Delete
    suspend fun eliminarCombo(combo: Combo)

    @Update
    suspend fun actualizarCombo(combo: Combo)

    // --- PRODUCTOS ---
    @Query("SELECT * FROM productos")
    fun obtenerTodosLosProductos(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)

    // --- INSUMOS ---
    @Query("SELECT * FROM insumos")
    fun obtenerTodosLosInsumos(): Flow<List<Insumo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInsumo(insumo: Insumo)

    @Update
    suspend fun actualizarInsumo(insumo: Insumo)

    @Delete
    suspend fun eliminarInsumo(insumo: Insumo)

    // --- MISIÓN DIARIA ---
    @Query("SELECT * FROM mision_diaria WHERE fechaCodigo = :fechaCodigo LIMIT 1")
    fun obtenerMisionDelDia(fechaCodigo: String): Flow<List<MisionDiaria>>

    @Query("SELECT * FROM mision_diaria WHERE fechaCodigo = :fechaCodigo LIMIT 1")
    suspend fun obtenerMisionDelDiaDirecta(fechaCodigo: String): MisionDiaria?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarMisionDiaria(mision: MisionDiaria)

    @Update
    suspend fun actualizarMisionDiaria(mision: MisionDiaria)

    // --- MOVIMIENTOS (INGRESOS Y EGRESOS) ---
    @Query("SELECT * FROM movimientos ORDER BY id DESC")
    fun obtenerTodosLosMovimientos(): Flow<List<Movimiento>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registrarMovimiento(movimiento: Movimiento)

    @Update
    suspend fun actualizarMovimiento(movimiento: Movimiento)

    @Delete
    suspend fun eliminarMovimiento(movimiento: Movimiento)

    // --- RECETA Y COSTOS ---
    @Query("SELECT * FROM receta_items ORDER BY id ASC")
    fun obtenerTodosLosIngredientesReceta(): Flow<List<RecetaItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIngredienteReceta(item: RecetaItem)

    @Update
    suspend fun actualizarIngredienteReceta(item: RecetaItem)

    @Delete
    suspend fun eliminarIngredienteReceta(item: RecetaItem)

  }

