package com.example.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}