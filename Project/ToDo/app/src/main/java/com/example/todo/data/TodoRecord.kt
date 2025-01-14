package com.example.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoRecord(
    @PrimaryKey
    val uid: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "deadline")
    val deadline: Long,

    @ColumnInfo(name = "status")
    val status: String,
)