package com.umc.sweepic.presentation.record.TagBord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class ImgGroupRVA(
    private val data: Map<String, List<String>>,
    private val tagsByDate: Map<String, List<String>>) :
    RecyclerView.Adapter<ImgGroupRVA.GroupViewHolder>() {

    private val groupedData = data.entries.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val (date, images) = groupedData[position]
        val tags = tagsByDate[date] ?: emptyList() // 태그 예제 (데이터로 대체 가능)
        holder.bind(date, images, tags)
    }

    override fun getItemCount(): Int = groupedData.size

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val chipRecyclerView: RecyclerView = itemView.findViewById(R.id.rc_chip)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.rc_img)

        fun bind(date: String, images: List<String>, tags: List<String>) {
            // 날짜 설정
            dateTextView.text = date

            // 태그 RecyclerView 설정
            chipRecyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            chipRecyclerView.adapter = ChipAdapter(tags)


            // 이미지 GridLayoutManager
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 5)
            recyclerView.adapter = ImgAdapter(images)
        }
    }
}

