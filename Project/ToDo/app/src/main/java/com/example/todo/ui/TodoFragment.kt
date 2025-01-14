package com.example.todo.ui

import TodoAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.data.TodoItem
import com.example.todo.data.TodoRecord
import com.example.todo.databinding.FragmentTodoBinding
import com.example.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class TodoFragment : Fragment(R.layout.fragment_todo) {
    private val viewModel: TodoViewModel by activityViewModels()
    private lateinit var binding: FragmentTodoBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoAdapter = TodoAdapter(
            deleteTodoRecord = { todoRecord: TodoRecord ->
                viewModel.deleteTodoRecord(todoRecord)
            },
            gotoEditorFn = { args: Bundle ->
                findNavController().navigate(R.id.action_fragmentTodo_to_fragmentEditor, args)
            },
            localState = LocaleManager.getLocale(requireContext())
        )

        binding.todoListView.adapter = todoAdapter

        val items = arrayOf(getString(R.string.all_tasks), getString(R.string.not_started), getString(R.string.in_progress), getString(R.string.done))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.statusSpinner.adapter = adapter

        binding.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                viewModel.setSelectedStatus(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val languageItems = arrayOf("English", "Русский")
        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languageItems)
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = languageAdapter

        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = when (position) {
                    0 -> "en"
                    1 -> "ru"
                    else -> "en"
                }
                (activity as? MainActivity)?.changeLanguage(selectedLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val currentLocale = LocaleManager.getLocale(requireContext())

        val position = when (currentLocale) {
            "en" -> 0
            "ru" -> 1
            else -> 0
        }

        binding.languageSpinner.setSelection(position)

        binding.createButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTodo_to_fragmentEditor)
        }

        binding.viewCreateButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTodo_to_fragmentEditor)
        }

        lifecycleScope.launch {
            viewModel.filteredRecords.collect { filteredRecords ->
                val tomorrow = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val dayAfterTomorrow = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 2)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val tasksDueTomorrow = filteredRecords.filter { it.deadline in tomorrow until dayAfterTomorrow }
                val futureTasks = filteredRecords.filter { it.deadline >= dayAfterTomorrow }

                val itemList = mutableListOf<TodoItem>()

                if (tasksDueTomorrow.isNotEmpty()) {
                    itemList.add(TodoItem.Header(getString(R.string.tomorrow)))
                    itemList.addAll(tasksDueTomorrow.map { TodoItem.Task(it) })
                }

                if (futureTasks.isNotEmpty()) {
                    itemList.add(TodoItem.Header(getString(R.string.future)))
                    itemList.addAll(futureTasks.map { TodoItem.Task(it) })
                }

                todoAdapter.todoItemList = itemList
                todoAdapter.notifyDataSetChanged()

                updateVisibility(itemList.isEmpty())
            }
        }
    }

    private fun updateVisibility(isEmpty: Boolean) {
        binding.viewTodo.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.emptyTodo.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}