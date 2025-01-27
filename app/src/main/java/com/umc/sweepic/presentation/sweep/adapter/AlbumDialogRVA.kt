package com.umc.sweepic.presentation.sweep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.databinding.ItemExistingFolderBinding
import com.umc.sweepic.domain.model.sweep.AlbumList

class AlbumDialogRVA(
    private val onAlbumSelectionChanged: (AlbumList, Boolean) -> Unit // 선택 상태 변경 콜백
) : ListAdapter<AlbumList, AlbumDialogRVA.AlbumViewHolder>(AlbumDiffCallback()) {

    private val selectedAlbums = mutableSetOf<AlbumList>() // 선택된 앨범 목록

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemExistingFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumViewHolder(private val binding: ItemExistingFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumList) {
            // 현재 앨범이 선택된 상태인지 체크
            binding.tvAlbumName.text = album.name
            binding.rbAlbumSelect.isChecked = selectedAlbums.contains(album)

            // 라디오 버튼 클릭 처리
            binding.rbAlbumSelect.setOnClickListener {
                handleSelectionChange(album)
            }

            // 전체 아이템 클릭 처리
            binding.root.setOnClickListener {
                binding.rbAlbumSelect.performClick() // 라디오 버튼 클릭 동작 호출
            }
        }

        private fun handleSelectionChange(album: AlbumList) {
            if (selectedAlbums.contains(album)) {
                // 이미 선택된 경우: 선택 해제
                selectedAlbums.remove(album)
                binding.rbAlbumSelect.isChecked = false
                onAlbumSelectionChanged(album, false)
            } else {
                // 선택되지 않은 경우: 선택 추가
                selectedAlbums.add(album)
                binding.rbAlbumSelect.isChecked = true
                onAlbumSelectionChanged(album, true)
            }
        }
    }

    // 초기 선택 상태 설정
    fun setSelectedAlbums(albums: Set<AlbumList>) {
        selectedAlbums.clear()
        selectedAlbums.addAll(albums)
        notifyDataSetChanged() // RecyclerView 갱신
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