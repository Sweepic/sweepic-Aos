package com.umc.sweepic.presentation.record.history.award

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemChoicePhotoBinding

/** 📌 RecyclerView에서 사진을 표시하는 Adapter */
class ChoicePhotoAdapter(
    private val onPhotoSelected: (SelectedPhoto) -> Unit // ✅ 타입 변경
) : ListAdapter<SelectedPhoto, ChoicePhotoAdapter.PhotoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemChoicePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(private val binding: ItemChoicePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: SelectedPhoto) {
            // Glide를 사용하여 이미지 로드
            Glide.with(binding.root.context)
                .load(photo.photoPath)
                .into(binding.ivPhoto)

            // 🔥 클릭 이벤트 추가
            binding.root.setOnClickListener {
                Log.d("PhotoAdapter", "📌 사진 클릭됨: ${photo.photoPath}") // 로그 확인
                onPhotoSelected(photo)
            }
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SelectedPhoto>() { // ✅ 타입 변경
            override fun areItemsTheSame(oldItem: SelectedPhoto, newItem: SelectedPhoto): Boolean {
                return oldItem.mediaId == newItem.mediaId // ✅ mediaId가 같으면 동일한 아이템으로 판단
            }

            override fun areContentsTheSame(oldItem: SelectedPhoto, newItem: SelectedPhoto): Boolean {
                return oldItem == newItem // ✅ 객체 자체가 동일한지 확인
            }
        }
    }
}
