package com.example.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAll(): List<Category>

    @Query("SELECT * FROM category")
    fun getAllAsFlow(): Flow<List<Category>>

    @Insert
    suspend fun insertAll(vararg category: Category)

    @Query("SELECT * FROM category WHERE name = :categoryName LIMIT 1")
    suspend fun findByName(categoryName: String): Category?
}