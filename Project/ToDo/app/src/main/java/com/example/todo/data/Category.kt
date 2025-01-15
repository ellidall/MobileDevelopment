package com.example.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,
)