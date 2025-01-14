package com.example.todo.data

sealed class TodoItem {
    data class Header(val title: String) : TodoItem()
    data class Task(val todoRecord: TodoRecord) : TodoItem()
}
