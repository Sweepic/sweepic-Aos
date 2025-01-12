package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemMoveGalleryBinding
import com.umc.sweepic.domain.model.sweep.Gallery

class GalleryRVA(
    private val onItemClick: (Gallery) -> Unit
): PagingDataAdapter<Gallery, GalleryRVA.ImageViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Gallery>() {
            override fun areItemsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem.uri == newItem.uri

            override fun areContentsTheSame(oldItem: Gallery, newItem: Gallery) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemMoveGalleryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class ImageViewHolder(
        private val binding: ItemMoveGalleryBinding,
        private val onItemClick: (Gallery) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Gallery) {
            binding.run {
                Glide.with(binding.root.context)
                    .load(item.uri)
                    .into(binding.ivGalleryImage)
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
