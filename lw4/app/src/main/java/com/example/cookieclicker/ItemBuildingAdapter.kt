package com.example.cookieclicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookieclicker.databinding.ItemBuildingBinding

data class ItemBuilding(
    val name: String,
    val icon: Int,
    val price: Int,
    var count: Int,
    val increase: Int,
    var isPurchasable: Boolean = false
) {
    fun totalIncrease(): Int {
        return count * increase
    }
}

class ItemBuildingAdapter(
    private val onBuildingClick: (ItemBuilding) -> Unit
) : ListAdapter<ItemBuilding, ItemBuildingAdapter.ItemViewHolder>(ItemDiffCallback()) {

    inner class ItemViewHolder(private val binding: ItemBuildingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemBuilding) {
            binding.tvName.text = item.name
            binding.tvPrice.text = item.price.toString()
            binding.tvCount.text = item.count.toString()
            binding.ivItemIcon.setImageResource(item.icon)

            binding.root.alpha = if (item.isPurchasable) 1.0f else 0.5f
            binding.root.setOnClickListener {
                onBuildingClick(item)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<ItemBuilding>() {
        override fun areItemsTheSame(oldItem: ItemBuilding, newItem: ItemBuilding): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ItemBuilding, newItem: ItemBuilding): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemBuildingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

