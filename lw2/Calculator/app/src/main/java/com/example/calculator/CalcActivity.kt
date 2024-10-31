package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import net.objecthunter.exp4j.ExpressionBuilder
import com.example.calculator.databinding.ActivityCalcBinding

class CalcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalcBinding
    private var expression = ""
    private var expressionText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
            binding.btnAdd, binding.btnSubtract, binding.btnMultiply, binding.btnDivide,
            binding.btnDecimal
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                appendToExpression((it as Button).text.toString())
            }
        }

        binding.btnClear.setOnClickListener {
            removeLastCharacter()
        }
    }

    private fun appendToExpression(value: String) {
        val correctedValue = when (value) {
            "×" -> "*"
            "÷" -> "/"
            else -> value
        }
        expression += correctedValue
        expressionText += value
        updateExpression()
    }

    private fun removeLastCharacter() {
        if (expression.isNotEmpty()) {
            expression = expression.substring(0, expression.length - 1)
            expressionText = expressionText.substring(0, expressionText.length - 1)
        }
        updateExpression()
    }

    private fun updateExpression() {
        binding.expressionTextView.text = expressionText
        calculateResult()
    }

    private fun calculateResult() {
        try {
            val expr = ExpressionBuilder(expression).build()
            val result = expr.evaluate()

            binding.expressionTextView.setTextColor(getColor(R.color.black))
            binding.resultTextView.setTextColor(getColor(R.color.black))
            binding.resultTextView.text = result.toString()
        } catch (e: Exception) {
            binding.expressionTextView.setTextColor(getColor(R.color.red))
            binding.resultTextView.setTextColor(getColor(R.color.red))
            binding.resultTextView.text = "Ошибка"
        }
    }
}
