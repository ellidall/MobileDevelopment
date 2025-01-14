package com.example.todo

import android.app.Application
import com.example.todo.data.AppDatabase

class StorageApp : Application() {
    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(this)
    }
}