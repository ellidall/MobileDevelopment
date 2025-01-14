package com.example.dictionary

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class SettingsStorage(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "settings")
    private val pinCodeKey = stringPreferencesKey("pin_code")

    suspend fun savePinCode(pinCode: String) {
        context.dataStore.edit {
            it[pinCodeKey] = pinCode
        }
    }

    suspend fun getPinCode(): String? {
        return context.dataStore.data.firstOrNull()?.get(pinCodeKey)
    }

    suspend fun removePinCode() {
        context.dataStore.edit { preferences ->
            preferences.remove(pinCodeKey)
        }
    }
}
