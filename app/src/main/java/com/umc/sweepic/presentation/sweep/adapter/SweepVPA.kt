package com.umc.sweepic.presentation.sweep.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemSweepImageBinding
import com.umc.sweepic.domain.model.sweep.Gallery

class SweepVPA(
    private val images: List<Gallery>
) : RecyclerView.Adapter<SweepVPA.SweepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SweepViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemSweepImageBinding.inflate(inflater, parent, false)
        return SweepViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SweepViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class SweepViewHolder(
        private val binding: ItemSweepImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Gallery) {
            // Glide 등으로 이미지 로드
            Glide.with(binding.root.context)
                .load(item.uri)
                .into(binding.ivSweepImage)
        }
    }
}
