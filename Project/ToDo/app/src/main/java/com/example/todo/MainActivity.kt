package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var currentLocale: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentLocale = LocaleManager.getLocale(this)
        LocaleManager.setLocale(this, currentLocale)

        setContentView(R.layout.activity_main)
    }

    fun changeLanguage(localeCode: String) {
        if (currentLocale != localeCode) {
            LocaleManager.setLocale(this, localeCode)
            currentLocale = localeCode

            updateUI()
            recreate()
        }
    }

    private fun updateUI() {
        title = getString(R.string.app_name)
    }
}