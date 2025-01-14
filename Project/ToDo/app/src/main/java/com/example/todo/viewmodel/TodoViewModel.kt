package com.example.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TodoRecord
import com.example.todo.StorageApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class TodoViewModel : ViewModel() {
    val records: Flow<List<TodoRecord>> = StorageApp.db.todoRecordDao().getAllAsFlow()
    private val selectedStatus = MutableStateFlow("All tasks")

    val filteredRecords: Flow<List<TodoRecord>> =
        combine(records, selectedStatus) { todoRecords, status ->
            if (status == "All tasks") {
                todoRecords
            } else {
                todoRecords.filter { it.status == status }
            }
        }

    fun setSelectedStatus(status: String) {
        when (status) {
            "Все задачи" -> selectedStatus.value = "All tasks"
            "Не начато" -> selectedStatus.value = "Not started"
            "В процессе" -> selectedStatus.value = "In progress"
            "Готово" -> selectedStatus.value = "Done"
            else -> selectedStatus.value = status
        }
    }

    fun createTodoRecord(title: String, content: String, deadline: Long, status: String) {
        viewModelScope.launch {

            val todoRecordDao = StorageApp.db.todoRecordDao()
            if (todoRecordDao.findByTitle(title) != null) {
                return@launch
            }

            val randomUid = UUID.randomUUID().toString()
            val newTodoRecord = TodoRecord(
                randomUid,
                title,
                content,
                deadline,
                status,
            )

            todoRecordDao.insertAll(newTodoRecord)
        }
    }

    fun updateTodoRecord(
        uid: String,
        title: String,
        content: String,
        deadline: Long,
        status: String
    ) {
        viewModelScope.launch {
            val todoRecordDao = StorageApp.db.todoRecordDao()
            val todoRecord = TodoRecord(
                uid,
                title,
                content,
                deadline,
                status,
            )

            todoRecordDao.updateAll(todoRecord)
        }
    }

    fun deleteTodoRecord(todoRecord: TodoRecord) {
        viewModelScope.launch {
            val todoRecordDao = StorageApp.db.todoRecordDao()
            todoRecordDao.delete(todoRecord)
        }
    }
}