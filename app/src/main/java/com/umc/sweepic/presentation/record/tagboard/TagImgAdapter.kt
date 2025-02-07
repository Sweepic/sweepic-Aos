package com.umc.sweepic.presentation.record.tagboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.R

class TagImgAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<TagImgAdapter.TagImgViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagImgViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag_img, parent, false)
        return TagImgViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagImgViewHolder, position: Int) {
        val imageUrl = imageList[position]

        // 이미지 크기 동적으로 조정 (정사각형)
        val layoutParams = holder.imageView.layoutParams
        val screenWidth = holder.itemView.context.resources.displayMetrics.widthPixels
        val itemSize = screenWidth / 2 // 2열 정사각형
        layoutParams.width = itemSize
        layoutParams.height = itemSize
        holder.imageView.layoutParams = layoutParams

        // Glide를 사용하여 이미지 로드
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_ex_image) // 기본 이미지 설정 가능
            .into(holder.imageView)
    }


    override fun getItemCount(): Int = imageList.size

    class TagImgViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_thumbnail)
    }

}
