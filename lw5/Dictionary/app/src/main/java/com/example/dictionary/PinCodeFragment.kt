package com.example.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentPinCodeBinding
import com.example.dictionary.viewmodel.LoginState
import com.example.dictionary.viewmodel.LoginViewModel
import com.example.dictionary.viewmodel.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PinCodeViewModelFactory(private val settingStorage: SettingsStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(settingStorage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PinCodeFragment : Fragment(R.layout.fragment_pin_code) {

    private lateinit var binding: FragmentPinCodeBinding

    private val viewModel: LoginViewModel by activityViewModels {
        PinCodeViewModelFactory(SettingsStorage(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPinCodeBinding.bind(view)

        lifecycleScope.launch {
            viewModel.loadActualPinCode()
        }

        viewModel.state
            .onEach { loginState ->
                updatePinCodeView(loginState)

                if ((loginState.currentState == State.ENTER_PIN || loginState.currentState == State.REPEAT_PIN) && loginState.pinCode.length == 4 && !loginState.isError) {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.pinCodeFragment, true)
                        .build()
                    findNavController().navigate(R.id.action_fragmentPinCode_to_fragmentDiary, null, navOptions)
                }

                if (loginState.isError) {
                    Toast.makeText(requireContext(), "Неверный PIN-код", Toast.LENGTH_SHORT).show()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        val buttons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6, binding.button7,
            binding.button8, binding.button9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.appendToPinCode(button.text.toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePinCodeView(loginState: LoginState) {
        when (loginState.currentState) {
            State.ENTER_PIN -> binding.tvTitle.text = "Введите PIN-код"
            State.CREATE_PIN -> binding.tvTitle.text = "Придумайте PIN-код"
            State.REPEAT_PIN -> binding.tvTitle.text = "Повторите PIN-код"
        }

        val pinLength = loginState.pinCode.length
        binding.dot1.setBackgroundResource(if (pinLength > 0) R.drawable.ic_dot else R.drawable.ic_dot_empty)
        binding.dot2.setBackgroundResource(if (pinLength > 1) R.drawable.ic_dot else R.drawable.ic_dot_empty)
        binding.dot3.setBackgroundResource(if (pinLength > 2) R.drawable.ic_dot else R.drawable.ic_dot_empty)
        binding.dot4.setBackgroundResource(if (pinLength > 3) R.drawable.ic_dot else R.drawable.ic_dot_empty)
    }
}