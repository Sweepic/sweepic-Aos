package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.databinding.ItemSweepTagBinding

class SweepTagRVA (

): ListAdapter<TagItem, SweepTagRVA.ViewHolder>(DiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSweepTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemSweepTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TagItem) {
            binding.tvSweepTagName.text = item.tagName
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TagItem>() {
        override fun areItemsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
            return oldItem == newItem

        }

    }
}

data class TagItem(
    val tagName: String
)