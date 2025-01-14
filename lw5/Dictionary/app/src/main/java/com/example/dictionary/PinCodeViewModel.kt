package com.example.dictionary

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

enum class State {
    CREATE_PIN,
    REPEAT_PIN,
    ENTER_PIN,
}

data class LoginState(
    val currentState: State,
    val pinCode: String = "",
    val isError: Boolean = false
)

class PinCodeViewModel(private val settingStorage: SettingsStorage) : ViewModel() {
    private val _state = MutableStateFlow(LoginState(State.CREATE_PIN))
    val state = _state.asStateFlow()

    private var actualPinCode: String? = null
        private set

    fun appendToPinCode(value: String) {
        val newPinCode = _state.value.pinCode + value
        updateState(newPinCode)
    }

    private fun updateState(newPinCode: String) {
        val currentState = _state.value.currentState

        when (currentState) {
            State.ENTER_PIN -> {
                if (newPinCode.length == 4) {
                    if (newPinCode == actualPinCode) {
                        _state.value = _state.value.copy(pinCode = newPinCode, isError = false)
                    } else {
                        _state.value = _state.value.copy(pinCode = "", isError = true)
                    }
                } else {
                    _state.value = _state.value.copy(pinCode = newPinCode, isError = false)
                }
            }
            State.CREATE_PIN -> {
                if (newPinCode.length == 4) {
                    actualPinCode = newPinCode
                    _state.value = _state.value.copy(pinCode = "", currentState = State.REPEAT_PIN, isError = false)
                } else {
                    _state.value = _state.value.copy(pinCode = newPinCode, isError = false)
                }
            }
            State.REPEAT_PIN -> {
                if (newPinCode.length == 4) {
                    if (newPinCode == actualPinCode) {
                        viewModelScope.launch {
                            settingStorage.savePinCode(newPinCode)
                        }
                        _state.value = _state.value.copy(pinCode = newPinCode, isError = false)
                    } else {
                        _state.value = _state.value.copy(pinCode = "", currentState = State.CREATE_PIN, isError = true)
                    }
                } else {
                    _state.value = _state.value.copy(pinCode = newPinCode, isError = false)
                }
            }
        }
    }

    suspend fun loadActualPinCode() {
        actualPinCode = settingStorage.getPinCode()
        _state.value = if (actualPinCode.isNullOrBlank()) {
            LoginState(State.CREATE_PIN)
        } else {
            LoginState(State.ENTER_PIN)
        }
    }
}
