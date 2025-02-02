package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.databinding.ItemSweepFolderBinding
import com.umc.sweepic.domain.model.sweep.AlbumList

class AlbumListRVA(
    private val onAlbumClick: (AlbumList) -> Unit // 앨범 클릭 이벤트
): ListAdapter<AlbumList, AlbumListRVA.AlbumViewHolder>(AlbumDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemSweepFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album)
    }

    inner class AlbumViewHolder(
        private val binding: ItemSweepFolderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumList) {
            // 앨범 이름 설정
            binding.tvSweepFolderName.text = album.name

            // 클릭 이벤트 처리
            binding.root.setOnClickListener {
                onAlbumClick(album)
            }
        }
    }

    class AlbumDiffCallback : DiffUtil.ItemCallback<AlbumList>() {
        override fun areItemsTheSame(oldItem: AlbumList, newItem: AlbumList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlbumList, newItem: AlbumList): Boolean {
            return oldItem == newItem
        }
    }
}