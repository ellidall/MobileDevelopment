package com.example.todo.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentCreateRecordBinding
import com.example.todo.viewmodel.TodoViewModel
import java.util.Date
import java.util.Locale

class EditorFragment : Fragment(R.layout.fragment_create_record) {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentCreateRecordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateRecordBinding.bind(view)

        val items = arrayOf(getString(R.string.not_started), getString(R.string.in_progress), getString(R.string.done))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.recordSpinner.adapter = adapter

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        if (arguments == null) {
            binding.createButton.setOnClickListener { onCreateButtonClick() }
        } else {
            binding.recordTitle.setText(requireArguments().getCharSequence("TITLE"))
            binding.recordContent.setText(requireArguments().getCharSequence("CONTENT"))
            val status = when (requireArguments().getString("STATUS")) {
                "All tasks" -> "Все задачи"
                "Not started" -> "Не начато"
                "In progress" -> "В процессе"
                "Done" -> "Готово"
                else -> "Неизвестно"
            }
            val statusPosition = items.indexOf(status)
            if (statusPosition >= 0) {
                binding.recordSpinner.setSelection(statusPosition)
            }
            val dateInMillis = requireArguments().getLong("DEADLINE")
            val date = Date(dateInMillis)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formatedDate = dateFormat.format(date)
            binding.selectedDateText.setText(getString(R.string.date_choosed) + " " + formatedDate)
            binding.createButton.setOnClickListener { updateTodoRecord(
                uid = requireArguments().getString("ID") as String,
            ) }
        }

        binding.selectDateButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun updateTodoRecord(uid: String) {
        if (binding.recordTitle.text.isEmpty()) {
            Toast.makeText(context, "Название не должно быть пустым", Toast.LENGTH_SHORT).show()
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

        viewModel.updateTodoRecord(
            uid,
            title = binding.recordTitle.text.toString(),
            content = binding.recordContent.text.toString(),
            deadline = dateInMillis,
            status = status,
        )

        findNavController().popBackStack()
    }

    private fun onCreateButtonClick() {
        if (binding.recordTitle.text.isEmpty()) {
            Toast.makeText(context, "Название не должно быть пустым", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedDateText = binding.selectedDateText.text.toString()
        val dateString = selectedDateText.substringAfter(": ").trim()
        println(dateString)
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

        viewModel.createTodoRecord(
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString(),
            dateInMillis,
            status,
        )

        findNavController().popBackStack()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.selectedDateText.text = getString(R.string.date_choosed) + " ${selectedDay}.${selectedMonth + 1}.$selectedYear"
    }, year, month, day).show()
}
}