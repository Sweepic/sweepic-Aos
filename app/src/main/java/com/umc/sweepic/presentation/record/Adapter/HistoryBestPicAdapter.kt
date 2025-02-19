package com.umc.sweepic.presentation.record.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class HistoryBestPicAdapter(
    private var imageResIds: List<Int>
) : RecyclerView.Adapter<HistoryBestPicAdapter.BestPictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestPictureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_bestpic, parent, false)
        return BestPictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: BestPictureViewHolder, position: Int) {
        holder.bind(imageResIds[position])
    }

    override fun getItemCount(): Int = imageResIds.size

    fun updateData(newImages: List<Int>) {
        imageResIds = newImages
        notifyDataSetChanged()
    }

    inner class BestPictureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.iv_best_picture)

        fun bind(imageResId: Int) {
            imageView.setImageResource(imageResId)
        }
    }
}
