package com.umc.sweepic.presentation.record.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemMemoFolderBinding

class MemoAdapter(
    private var memoFolders: List<MemoFolder>,
    private val onItemClick: (MemoFolder) -> Unit
) : RecyclerView.Adapter<MemoAdapter.MemoFolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoFolderViewHolder {
        val binding = ItemMemoFolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemoFolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoFolderViewHolder, position: Int) {
        holder.bind(memoFolders[position])
    }

    override fun getItemCount(): Int = memoFolders.size

    fun updateData(newData: List<MemoFolder>) {
        memoFolders = newData
        notifyDataSetChanged()
    }

    inner class MemoFolderViewHolder(private val binding: ItemMemoFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memoFolder: MemoFolder) {
            binding.tvMemoFolderTitle.text = memoFolder.title
            binding.tvMemoDate.text = memoFolder.date
            binding.tvMemoContent.text = memoFolder.content ?: ""

            // ✅ 이미지가 있을 경우만 로드
            if (!memoFolder.imageUrl.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(memoFolder.imageUrl)
                    .into(binding.memoImage)
                binding.memoImage.visibility = View.VISIBLE
            } else {
                binding.memoImage.visibility = View.GONE
            }

            binding.root.setOnClickListener { onItemClick(memoFolder) }
        }
    }
}
