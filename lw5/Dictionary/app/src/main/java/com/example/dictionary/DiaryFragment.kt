package com.example.dictionary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentDiaryBinding
import kotlinx.coroutines.launch

class DiaryFragment : Fragment(R.layout.fragment_diary) {
    private val viewModel: RecordViewModel by activityViewModels()
    private lateinit var binding: FragmentDiaryBinding
    private lateinit var adapter: RecordAdapter

    private var selectedDateMillis: Long? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDiaryBinding.bind(view)
        adapter = RecordAdapter(
            onDeleteClick = { diaryRecord: RecordItem ->
                viewModel.deleteDiaryRecord(diaryRecord)
            },
            gotoEditor = { args: Bundle ->
                findNavController().navigate(R.id.action_fragmentDiary_to_fragmentEditor, args)
            }
        )
        binding.diary.adapter = adapter

        binding.addRecord.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDiary_to_fragmentEditor)
        }

        binding.createEntryButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentDiary_to_fragmentEditor)
        }

        binding.filterIcon.setOnClickListener {
            showDatePicker()
        }

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            requireActivity().finish()
//        }

        lifecycleScope.launch {
            viewModel.filteredRecords.collect { filteredRecords ->
                adapter.recordList = filteredRecords.sortedByDescending { it.date }
                adapter.notifyDataSetChanged()
                updateVisibility(filteredRecords.isEmpty())
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            viewModel.setSelectedDate(selectedDate.timeInMillis)
        }, year, month, day).show()
    }

    private fun updateVisibility(isEmpty: Boolean) {
        binding.diary.visibility = View.GONE
        binding.emptyDiary.visibility = View.GONE
        if (isEmpty) {
            binding.emptyDiary.visibility = View.VISIBLE
            binding.diary.visibility = View.GONE
        } else {
            binding.emptyDiary.visibility = View.GONE
            binding.diary.visibility = View.VISIBLE
        }
    }
}