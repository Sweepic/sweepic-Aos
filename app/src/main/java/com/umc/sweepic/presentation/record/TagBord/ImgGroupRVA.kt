package com.umc.sweepic.presentation.record.TagBord

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class ImgGroupRVA(
    private val data: Map<String, List<String>>,
    private val tagsByDate: Map<String, List<String>>,
    private val onItemClick: (String, List<String>, List<String>) -> Unit) :
    RecyclerView.Adapter<ImgGroupRVA.GroupViewHolder>() {

    private val groupedData = data.entries.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val (date, images) = groupedData[position]
        val formattedDate = date.split("-")[1] // "yyyy-MM월 dd일" 형식에서 MM월 dd일 추출
        val tags = tagsByDate[date] ?: emptyList()

        holder.bind(formattedDate, images, tags, onItemClick)
    }

    override fun getItemCount(): Int = groupedData.size

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val chipRecyclerView: RecyclerView = itemView.findViewById(R.id.rc_chip)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.rc_img)
        private val btnNext: ImageView = itemView.findViewById(R.id.btn_next)

        fun bind(date: String, images: List<String>, tags: List<String>, onItemClick: (String, List<String>, List<String>) -> Unit) {
            // 날짜 설정
            dateTextView.text = date

            // 태그 RecyclerView 설정
            chipRecyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            chipRecyclerView.adapter = ChipAdapter(tags)

            // 이미지 RecyclerView 설정
            val spanCount = calculateSpanCount(itemView.context, 64)
            recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount)
            recyclerView.adapter = ImgAdapter(images)

            // ✅ btn_next 클릭 시 DetailImgFragment 이동
            btnNext.setOnClickListener {
                Log.d("ImgGroupRVA", "btn_next clicked, navigating with date: $date")
                onItemClick(date, images, tags)
            }
        }

        private fun calculateSpanCount(context: Context, itemWidthDp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenWidthPx = displayMetrics.widthPixels
            val screenWidthDp = screenWidthPx / displayMetrics.density
            return (screenWidthDp / itemWidthDp).toInt()
        }
    }
}

