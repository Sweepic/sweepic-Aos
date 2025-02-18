package com.umc.sweepic.presentation.record.history.award

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemBestPhotoBinding

class BestPhotoAdapter(private var photoList: List<String>) :
    RecyclerView.Adapter<BestPhotoAdapter.BestPhotoViewHolder>() {

    inner class BestPhotoViewHolder(private val binding: ItemBestPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photoUrl: String) {
            Glide.with(binding.ivBestPhoto.context)
                .load(photoUrl)
                .into(binding.ivBestPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestPhotoViewHolder {
        val binding =
            ItemBestPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestPhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size

    fun updateData(newPhotoList: List<String>) {
        photoList = newPhotoList
        notifyDataSetChanged()
    }
}
