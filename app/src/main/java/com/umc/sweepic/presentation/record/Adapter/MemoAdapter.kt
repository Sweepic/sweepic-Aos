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


            //사진 수 표시
            if (memoFolder.imageCount > 0) {
                binding.tvMemoPhotoNum.text = "${memoFolder.imageCount}장의 사진"
                binding.tvMemoPhotoNum.visibility = View.VISIBLE
            } else {
                binding.tvMemoPhotoNum.visibility = View.GONE // 🔹 사진 없으면 아예 안 보이게 설정
            }


            //메모 내용 표시
            if (!memoFolder.content.isNullOrEmpty()) {
                binding.tvMemoContent.text = memoFolder.content
                binding.tvMemoContent.visibility = View.VISIBLE
            } else {
                binding.tvMemoContent.visibility = View.GONE //
            }


            // 썸네일 이미지 표시 (이미지가 있을 경우만 로드하기)
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
