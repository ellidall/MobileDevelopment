package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.calculator.databinding.ActivityCalcBinding
import kotlinx.coroutines.flow.collectLatest

class CalcActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalcBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeState()
    }

    private fun setupUI() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
            binding.btnAdd, binding.btnSubtract, binding.btnMultiply, binding.btnDivide,
            binding.btnDecimal
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.appendToExpression((it as Button).text.toString())
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.removeLastCharacter()
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: CalculatorState) {
        binding.expressionTextView.text = state.displayExpression
        binding.resultTextView.text = state.result
        val textColor = if (state.isError) R.color.red else R.color.black
        binding.expressionTextView.setTextColor(getColor(textColor))
        binding.resultTextView.setTextColor(getColor(textColor))
    }
}
