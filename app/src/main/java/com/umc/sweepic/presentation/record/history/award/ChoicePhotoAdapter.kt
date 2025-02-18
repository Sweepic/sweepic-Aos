package com.umc.sweepic.presentation.record.history.award

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemChoicePhotoBinding

/** 📌 RecyclerView에서 사진을 표시하는 Adapter */
class ChoicePhotoAdapter : ListAdapter<String, ChoicePhotoAdapter.PhotoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemChoicePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(private val binding: ItemChoicePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoPath: String) {
            // Glide를 사용하여 이미지 로드
            Glide.with(binding.root.context)
                .load(photoPath)
                .into(binding.ivPhoto)

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}
