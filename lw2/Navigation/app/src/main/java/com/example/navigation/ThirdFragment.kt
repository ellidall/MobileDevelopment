package com.example.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(R.layout.fragment_third) {
    private lateinit var binding: FragmentThirdBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)

        val name = arguments?.getString("NAME")
        val surname = arguments?.getString("SURNAME")
        val birthDate = arguments?.getString("BIRTH_DATE")
        binding.displayTextView.text = "Name: $name\nSurname: $surname\nBirth Date: $birthDate"

        binding.backButton.setOnClickListener {
            findNavController().popBackStack(R.id.firstFragment, false)
        }
    }
}