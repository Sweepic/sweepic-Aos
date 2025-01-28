package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.databinding.ItemAddMemoFolderBinding

class SweepMemoFolderRVA(
    private val onFolderClick: (MemoFolderList) -> Unit
) : ListAdapter<MemoFolderList, SweepMemoFolderRVA.MemoFolderViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoFolderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddMemoFolderBinding.inflate(inflater, parent, false)
        return MemoFolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoFolderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MemoFolderViewHolder(
        private val binding: ItemAddMemoFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(folder: MemoFolderList) {
            binding.tvAddMemoFolderName.text = folder.name
            binding.root.setOnClickListener {
                onFolderClick(folder)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MemoFolderList>() {
        override fun areItemsTheSame(oldItem: MemoFolderList, newItem: MemoFolderList): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MemoFolderList, newItem: MemoFolderList): Boolean {
            return oldItem == newItem
        }
    }
}
data class MemoFolderList(val name: String)

