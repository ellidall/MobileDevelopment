package com.example.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.objecthunter.exp4j.ExpressionBuilder

data class CalculatorState(
    val expression: String = "",
    val displayExpression: String = "",
    val result: String = "",
    val isError: Boolean = false
)

class CalculatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    fun appendToExpression(value: String) {
        val correctedValue = when (value) {
            "×" -> "*"
            "÷" -> "/"
            else -> value
        }
        val newExpression = _state.value.expression + correctedValue
        val newDisplayExpression = _state.value.displayExpression + value
        _state.value = _state.value.copy(
            expression = newExpression,
            displayExpression = newDisplayExpression
        )
        calculateResult(newExpression)
    }

    fun removeLastCharacter() {
        val currentExpression = _state.value.expression
        val currentDisplay = _state.value.displayExpression
        if (currentExpression.isNotEmpty()) {
            val newExpression = currentExpression.dropLast(1)
            val newDisplayExpression = currentDisplay.dropLast(1)
            _state.value = _state.value.copy(
                expression = newExpression,
                displayExpression = newDisplayExpression,
                result = "",
                isError = false
            )
            calculateResult(newExpression)
        }
    }

    private fun calculateResult(expression: String) {
        if (expression.contains("/0")) {
            _state.value = _state.value.copy(
                result = "Ошибка: Деление на 0",
                isError = true
            )
            return
        }
        try {
            val expr = ExpressionBuilder(expression).build()
            val result = expr.evaluate()
            _state.value = _state.value.copy(
                result = result.toString(),
                isError = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                result = "Ошибка",
                isError = true
            )
        }
    }
}
