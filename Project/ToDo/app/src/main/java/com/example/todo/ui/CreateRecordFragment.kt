package com.example.todo.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentCreateRecordBinding
import com.example.todo.viewmodel.CategoryViewModel
import com.example.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class CreateRecordFragment : Fragment(R.layout.fragment_create_record) {
    private val todoRecordsViewModel: TodoViewModel by activityViewModels()
    private val categoriesViewModel: CategoryViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateRecordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRecordBinding.bind(view)

        val recordsSpinnerItems = arrayOf(
            getString(R.string.not_started),
            getString(R.string.in_progress),
            getString(R.string.done)
        )
        val recordsAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            recordsSpinnerItems
        )
        recordsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.recordSpinner.adapter = recordsAdapter

        val categorySpinnerItems = mutableListOf(getString(R.string.all_categories))
        lifecycleScope.launch {
            categoriesViewModel.actualCategories.collect { categoriesFromModel ->
                if (categoriesFromModel.isNotEmpty()) {
                    categoriesFromModel.map { categorySpinnerItems += it.name }
                }
            }
        }
        val categoriesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categorySpinnerItems
        )
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoriesAdapter

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        if (arguments == null) {
            binding.confirmButton.setOnClickListener { onCreateButtonClick() }
        } else {
            binding.recordTitle.setText(requireArguments().getString("TITLE"))
            binding.recordContent.setText(requireArguments().getString("CONTENT"))
            val status = when (requireArguments().getString("STATUS")) {
                "All tasks" -> "Все задачи"
                "Not started" -> "Не начато"
                "In progress" -> "В процессе"
                "Done" -> "Готово"
                else -> "Неизвестно"
            }
            val statusPosition = recordsSpinnerItems.indexOf(status)
            if (statusPosition >= 0) {
                binding.recordSpinner.setSelection(statusPosition)
            }

            Log.d("CATEGORY", requireArguments().getString("CATEGORY") ?: "null")
            val category = requireArguments().getString("CATEGORY")
            val categoryPosition = categorySpinnerItems.indexOf(category)
            Log.d("categoryPosition", categoryPosition.toString())
            if (categoryPosition >= 0) {
                binding.categorySpinner.setSelection(categoryPosition)
            }

            val dateInMillis = requireArguments().getLong("DEADLINE")
            val date = Date(dateInMillis)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formatedDate = dateFormat.format(date)
            binding.selectedDateText.setText(getString(R.string.date_chosen) + " " + formatedDate)
            binding.confirmButton.setOnClickListener {
                updateTodoRecord(
                    uid = requireArguments().getString("ID") as String,
                )
            }
        }

        binding.selectDateButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun updateTodoRecord(uid: String) {
        if (binding.recordTitle.text.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.toast_title_cannot_be_empty),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val selectedDateText = binding.selectedDateText.text.toString()
        val dateString = selectedDateText.substringAfter(": ").trim()
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val date: Date? = dateFormat.parse(dateString)
        val dateInMillis: Long = date?.time ?: 0L

        val status = when (binding.recordSpinner.selectedItem.toString()) {
            "Все задачи" -> "All tasks"
            "Не начато" -> "Not started"
            "В процессе" -> "In progress"
            "Готово" -> "Done"
            else -> binding.recordSpinner.selectedItem.toString()
        }

        todoRecordsViewModel.updateTodoRecord(
            uid,
            title = binding.recordTitle.text.toString(),
            content = binding.recordContent.text.toString(),
            deadline = dateInMillis,
            status = status,
            category = if (binding.categorySpinner.selectedItem != null && binding.categorySpinner.selectedItem != "All") {
                binding.categorySpinner.selectedItem.toString()
            } else {
                null
            }
        )

        findNavController().popBackStack()
    }

    private fun onCreateButtonClick() {
        if (binding.recordTitle.text.isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.toast_title_cannot_be_empty),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val selectedDateText = binding.selectedDateText.text.toString()
        val dateString = selectedDateText.substringAfter(": ").trim()
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date: Date? = dateFormat.parse(dateString)
        val dateInMillis: Long = date?.time ?: 0L

        val status = when (binding.recordSpinner.selectedItem.toString()) {
            "Все задачи" -> "All"
            "Не начато" -> "Not started"
            "В процессе" -> "In progress"
            "Готово" -> "Done"
            else -> binding.recordSpinner.selectedItem.toString()
        }

        Log.d("Ssasdsscscscsc", binding.categorySpinner.selectedItem.toString())
        todoRecordsViewModel.createTodoRecord(
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString(),
            dateInMillis,
            status,
            category = if (binding.categorySpinner.selectedItem != null && binding.categorySpinner.selectedItem != "All") {
                binding.categorySpinner.selectedItem.toString()
            } else {
                null
            }
        )

        findNavController().popBackStack()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.selectedDateText.text =
                getString(R.string.date_chosen) + " ${selectedDay}.${selectedMonth + 1}.$selectedYear"
        }, year, month, day).show()
    }
}