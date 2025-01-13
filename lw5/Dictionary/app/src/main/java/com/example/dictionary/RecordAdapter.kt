package com.example.dictionary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.ItemRecordBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view)

class RecordAdapter(
    private val gotoEditor: (arguments: Bundle) -> Unit,
    private val onDeleteClick: (RecordItem) -> Unit,
) : RecyclerView.Adapter<RecordViewHolder>() {
    var recordList = listOf<RecordItem>()

    override fun getItemCount() = recordList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordBinding.inflate(inflater, parent, false)

        return RecordViewHolder(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val itemBinding = ItemRecordBinding.bind(holder.itemView)
        val record = recordList[position]

        itemBinding.title.text = record.title
        itemBinding.description.text = record.description

        val createdAt = record.date
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault())
        val formattedDate = formatter.format(Instant.ofEpochMilli(createdAt))
        itemBinding.date.text = formattedDate


        holder.itemView.setOnClickListener {
            val arguments = Bundle().apply {
                putString("TITLE", record.title)
                putString("CONTENT", record.description)
                putString("ID", record.uid)
                putLong("DATE", record.date)
            }

            gotoEditor(arguments)
        }

        itemBinding.iconDelete.setOnClickListener {
            onDeleteClick(record)
        }
    }
}
