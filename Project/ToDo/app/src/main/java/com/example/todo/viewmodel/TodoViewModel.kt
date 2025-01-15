package com.example.todo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TodoRecord
import com.example.todo.StorageApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID

class TodoViewModel : ViewModel() {
    private val ALL_TODO_RECORDS = "All tasks"
    private val ALL_CATEGORIES = "All"
    private val todoRecordDao = StorageApp.db.todoRecordDao()

    private val _selectedStatus = MutableStateFlow("All tasks")
    private val _selectedCategory = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow<List<TodoRecord>>(emptyList())
    val actualTodoList: StateFlow<List<TodoRecord>> = _state

    init {
        viewModelScope.launch {
            _state.value = todoRecordDao.getAll()
        }

        viewModelScope.launch {
            val recordsFlow = todoRecordDao.getAllAsFlow()
            combine(
                _selectedStatus,
                _selectedCategory,
                recordsFlow
            ) { status, category, todoRecords ->
                var filteredList = todoRecords
                if (status != ALL_TODO_RECORDS) {
                    filteredList = filteredList.filter { it.status == status }
                }
                if (category != null && category != ALL_CATEGORIES) {
                    filteredList = filteredList.filter { it.categoryName == category }
                }
                filteredList
            }.collect { filteredRecords ->
                _state.value = filteredRecords
            }
        }
    }

    fun setSelectedStatus(status: String) {
        when (status) {
            "Все задачи" -> _selectedStatus.value = ALL_TODO_RECORDS
            "Не начато" -> _selectedStatus.value = "Not started"
            "В процессе" -> _selectedStatus.value = "In progress"
            "Готово" -> _selectedStatus.value = "Done"
            else -> _selectedStatus.value = status
        }
    }

    fun setSelectedCategory(category: String?) {
        when (category) {
            "Все" -> _selectedCategory.value = ALL_CATEGORIES
            else -> _selectedCategory.value = category
        }
    }

    fun createTodoRecord(
        title: String,
        content: String,
        deadline: Long,
        status: String,
        category: String? = null
    ) {
        viewModelScope.launch {
            val randomUid = UUID.randomUUID().toString()
            val newTodoRecord = TodoRecord(
                randomUid,
                title,
                content,
                deadline,
                status,
                category
            )
            todoRecordDao.insertAll(newTodoRecord)
        }
    }

    fun updateTodoRecord(
        uid: String,
        title: String,
        content: String,
        deadline: Long,
        status: String,
        category: String? = null
    ) {
        viewModelScope.launch {
            Log.d("UPPDAAATEEE", title + content + status + category)
            val todoRecord = TodoRecord(
                uid,
                title,
                content,
                deadline,
                status,
                category
            )
            todoRecordDao.updateAll(todoRecord)
        }
    }

    fun deleteTodoRecord(todoRecord: TodoRecord) {
        viewModelScope.launch { todoRecordDao.delete(todoRecord) }
    }
}