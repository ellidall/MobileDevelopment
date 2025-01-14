package com.example.todo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoRecordDao {
    @Query("SELECT * FROM todorecord")
    suspend fun getAll(): List<TodoRecord>

    @Query("SELECT * FROM todorecord")
    fun getAllAsFlow(): Flow<List<TodoRecord>>

    @Insert
    suspend fun insertAll(vararg todoRecord: TodoRecord)

    @Update
    suspend fun updateAll(vararg todoRecord: TodoRecord)

    @Query("SELECT * FROM todorecord " +
            "WHERE title LIKE :title LIMIT 1")
    suspend fun findByTitle(title: String): TodoRecord?

    @Delete
    suspend fun delete(record: TodoRecord)
}