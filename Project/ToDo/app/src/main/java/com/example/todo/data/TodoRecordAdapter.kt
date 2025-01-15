package com.example.todo.data

import android.os.Build

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TodoAdapter(
    private val gotoEditorFn: (todoRecord: TodoRecord ) -> Unit,
    private val deleteTodoRecord: (todoRecord: TodoRecord) -> Unit,
    private val localState: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var todoItemList = listOf<TodoItem>()

    override fun getItemCount(): Int {
        return todoItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (todoItemList[position]) {
            is TodoItem.Header -> 0
            is TodoItem.Task -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }

            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
                TaskViewHolder(view)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = todoItemList[position]) {
            is TodoItem.Header -> (holder as HeaderViewHolder).bind(item)
            is TodoItem.Task -> (holder as TaskViewHolder).bind(item.todoRecord)
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.headerText)

        fun bind(header: TodoItem.Header) {
            titleTextView.text = header.title
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.recordTitle)
        private val contentTextView: TextView = view.findViewById(R.id.recordContent)
        private val stateTextView: TextView = view.findViewById(R.id.record_state)
        private val dateTextView: TextView = view.findViewById(R.id.record_date)
        private val deleteButton: ImageButton = view.findViewById(R.id.record_delete_button)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(todoRecord: TodoRecord) {
            titleTextView.text = todoRecord.title
            contentTextView.text = todoRecord.content
            stateTextView.text = if (localState == "en") {
                todoRecord.status
            } else {
                when (todoRecord.status) {
                    "Not started" -> "Не начато"
                    "In progress" -> "В процессе"
                    "Done" -> "Готово"
                    else -> ""
                }
            }

            val deadline = todoRecord.deadline
            val formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault())
            val formattedDate = formatter.format(Instant.ofEpochMilli(deadline))

            dateTextView.text = formattedDate

            itemView.setOnClickListener {
                gotoEditorFn(todoRecord)
            }

            deleteButton.setOnClickListener {
                deleteTodoRecord(todoRecord)
            }
        }
    }
}
