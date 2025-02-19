package com.umc.sweepic.presentation.record.history.award

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ItemBestPhotoBinding
import com.umc.sweepic.databinding.ItemChoicePhotoBinding

/** 📌 RecyclerView에서 사진을 표시하는 Adapter */
class ChoicePhotoAdapter(
    private val onPhotoSelected: (SelectedPhoto) -> Unit,
    private val itemLayoutResId: Int // ✅ 레이아웃 리소스 ID 추가
) : ListAdapter<SelectedPhoto, ChoicePhotoAdapter.PhotoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (itemLayoutResId == R.layout.item_best_photo) {
            ItemBestPhotoBinding.inflate(inflater, parent, false) // ✅ `item_best_photo` 사용
        } else {
            ItemChoicePhotoBinding.inflate(inflater, parent, false) // ✅ 기본 `item_choice_photo` 사용
        }
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: SelectedPhoto) {
            // ✅ ViewBinding 타입에 따라 처리 방식 변경
            when (binding) {
                is ItemBestPhotoBinding -> {
                    Glide.with(binding.root.context)
                        .load(photo.photoPath)
                        .into(binding.ivBestPhoto)
                }
                is ItemChoicePhotoBinding -> {
                    Glide.with(binding.root.context)
                        .load(photo.photoPath)
                        .into(binding.ivPhoto)
                }
            }

            binding.root.setOnClickListener {
                Log.d("PhotoAdapter", "📌 사진 클릭됨: ${photo.photoPath}")
                onPhotoSelected(photo)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SelectedPhoto>() {
            override fun areItemsTheSame(oldItem: SelectedPhoto, newItem: SelectedPhoto): Boolean {
                return oldItem.mediaId == newItem.mediaId
            }

            override fun areContentsTheSame(oldItem: SelectedPhoto, newItem: SelectedPhoto): Boolean {
                return oldItem == newItem
            }
        }
    }
}


