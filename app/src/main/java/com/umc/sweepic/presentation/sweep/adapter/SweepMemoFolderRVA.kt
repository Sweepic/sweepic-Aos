package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.databinding.ItemAddMemoFolderBinding
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel

class SweepMemoFolderRVA(
    private val onFolderClick: (SweepMemoListModel.MemoFolderModel) -> Unit
) : ListAdapter<SweepMemoListModel.MemoFolderModel, SweepMemoFolderRVA.MemoFolderViewHolder>(DiffCallback()) {

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

        fun bind(folder: SweepMemoListModel.MemoFolderModel) {
            binding.tvAddMemoFolderName.text = folder.folderName
            binding.root.setOnClickListener {
                onFolderClick(folder)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SweepMemoListModel.MemoFolderModel>() {
        override fun areItemsTheSame(oldItem: SweepMemoListModel.MemoFolderModel, newItem: SweepMemoListModel.MemoFolderModel): Boolean {
            return oldItem.folderId == newItem.folderId
        }

        override fun areContentsTheSame(oldItem: SweepMemoListModel.MemoFolderModel, newItem: SweepMemoListModel.MemoFolderModel): Boolean {
            return oldItem == newItem
        }
    }
}

