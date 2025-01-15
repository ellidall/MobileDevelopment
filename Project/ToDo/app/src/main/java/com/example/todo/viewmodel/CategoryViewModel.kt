package com.example.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.StorageApp
import com.example.todo.data.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val categoryDao = StorageApp.db.categoryDao()
    private val _state = MutableStateFlow<List<Category>>(emptyList())
    val actualCategories: StateFlow<List<Category>> = _state

    init {
        viewModelScope.launch {
            _state.value = categoryDao.getAll()
        }

        viewModelScope.launch {
            categoryDao.getAllAsFlow().collect { categories ->
                _state.value = categories
            }
        }
    }

    fun createCategory(category: Category) {
        viewModelScope.launch { categoryDao.insertAll(category) }
    }

    suspend fun findCategoryByName(name: String): Category? {
        return categoryDao.findByName(name)
    }
}