package com.umc.sweepic.presentation.sweep.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemMoveGalleryBinding
import com.umc.sweepic.domain.model.sweep.Gallery

class TrashRVA(
    private val onSelectionChanged: (Int) -> Unit
): ListAdapter<Gallery, TrashRVA.ImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Gallery>() {
            override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem.uri == newItem.uri

            override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem == newItem
        }
    }
    private val selectedItems = mutableSetOf<Gallery>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemMoveGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, selectedItems.contains(item))
        }
    }

    inner class ImageViewHolder(
        private val binding: ItemMoveGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Gallery, isSelected: Boolean) {
            // 1) 아이템 이미지 로딩
            Glide.with(binding.root.context)
                .load(item.uri)
                .into(binding.ivGalleryImage)

            // 2) 선택 상태에 따른 UI 표시 (예: 테두리, 반투명, 체크 아이콘 등)
            if (isSelected) {
                binding.root.alpha = 0.5f // 단순 예시로 투명도 처리
            } else {
                binding.root.alpha = 1.0f
            }

            // 3) 아이템 클릭 시, 선택/해제
            binding.root.setOnClickListener {
                toggleSelection(item)
            }
        }
    }
    private fun toggleSelection(item: Gallery) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        // 선택 개수 콜백
        onSelectionChanged(selectedItems.size)
        // 갱신
        notifyDataSetChanged() // 또는 notifyItemChanged(...)로 부분 갱신
    }

    // Activity에서 사용하기 위한 헬퍼 메서드들
    fun getSelectedItems(): List<Gallery> = selectedItems.toList()

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }
}
