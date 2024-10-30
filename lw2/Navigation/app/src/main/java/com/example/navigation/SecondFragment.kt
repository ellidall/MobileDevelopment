package com.example.navigation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentSecondBinding
import java.util.Calendar

class SecondFragment : Fragment(R.layout.fragment_second) {
    private lateinit var binding: FragmentSecondBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)

        binding.displayNameTextView.text = arguments?.getString("NAME")
        binding.displaySurnameTextView.text = arguments?.getString("SURNAME")

        binding.openDatePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.nextButton.setOnClickListener {
            val name = binding.displayNameTextView.text.toString()
            val surname = binding.displaySurnameTextView.text.toString()
            val birthDate = binding.birthDateTextView.text.toString()

            val bundle = Bundle().apply {
                putString("NAME", name)
                putString("SURNAME", surname)
                putString("BIRTH_DATE", birthDate)
            }
            findNavController().navigate(R.id.action_secondFragment_to_thirdFragment, bundle)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.birthDateTextView.text = "$selectedDay/${selectedMonth + 1}/$selectedYear" // Месяцы начинаются с 0
        }, year, month, day)

        datePickerDialog.show()
    }
}