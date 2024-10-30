package com.example.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.navigation.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {
    private lateinit var binding: FragmentFirstBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        binding.openSecondFragmentButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val surname = binding.surnameEditText.text.toString()

            val arguments = Bundle().apply {
                putString("NAME", name)
                putString("SURNAME", surname)
            }

            findNavController().navigate(R.id.action_firstFragment_to_secondFragment, arguments)
        }
        binding.openFourthFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_FourthFragment)
        }
    }
}