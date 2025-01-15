package com.example.todo.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.data.Category
import com.example.todo.databinding.FragmentCreateCategoryBinding
import com.example.todo.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

class CreateCategoryFragment : Fragment(R.layout.fragment_create_category) {
    private val viewModel: CategoryViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateCategoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateCategoryBinding.bind(view)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.confirmButton.setOnClickListener {
            onCreateButtonClick()
        }
    }

    private fun onCreateButtonClick() {
        lifecycleScope.launch {
            val title = binding.categoryTitle.text.toString()
            if (title.isEmpty()) {
                Toast.makeText(context, getString(R.string.toast_title_cannot_be_empty), Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (viewModel.findCategoryByName(title) == null && title != "All") {
                viewModel.createCategory(Category(name = title))
            } else {
                Toast.makeText(context, getString(R.string.toast_this_category_was_created_earlier), Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
        }
    }
}