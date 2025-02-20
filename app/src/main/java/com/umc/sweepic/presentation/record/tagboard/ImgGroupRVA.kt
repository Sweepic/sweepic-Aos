package com.umc.sweepic.presentation.record.tagboard

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
    private var data: Map<String, List<String>>,
    private val tagsByDate: Map<String, List<String>>,
    private val onItemClick: (String, List<String>, List<String>) -> Unit,
    private val onTagClick: (String) -> Unit
) : RecyclerView.Adapter<ImgGroupRVA.GroupViewHolder>() {

    private val groupedData = data.entries.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_group, parent, false)
        return GroupViewHolder(view,onTagClick)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val (date, images) = groupedData[position]
        val key = if (!date.contains("-")) "2025-$date" else date // ✅ 연도 추가

        val tags = tagsByDate[key] ?: emptyList()

        Log.d("ImgGroupRVA", "✅ onBindViewHolder - 날짜: $key, 조회된 태그: $tags")

        holder.bind(key, images.distinct(), tags, onItemClick)
    }

    override fun getItemCount(): Int = groupedData.size

    fun updateData(newData: Map<String, List<String>>) {
        data = newData // ✅ 기존 데이터를 변경하지 않고 새로운 데이터만 적용
        notifyDataSetChanged() // ✅ UI 갱신
    }

    class GroupViewHolder(itemView: View, private val onTagClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val dateTextView: TextView = itemView.findViewById(R.id.tv_date)
        private val chipRecyclerView: RecyclerView = itemView.findViewById(R.id.rc_chip)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.rc_img)
        private val btnNext: ImageView = itemView.findViewById(R.id.btn_next)

        fun bind(date: String,
                 images: List<String>,
                 tags: List<String>,
                 onItemClick: (String, List<String>, List<String>) -> Unit
        ) {
            Log.d("ImgGroupRVA", "bind 호출: 날짜 = $date, 태그 = $tags")
            // 날짜 설정
            dateTextView.text = date

            // 태그 RecyclerView 설정
            chipRecyclerView.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val chipAdapter = ChipAdapter(tags.toMutableList(), isDetail = false, onTagClick)
            chipRecyclerView.adapter = chipAdapter

            chipAdapter.notifyDataSetChanged()

            // 이미지 RecyclerView 설정
            val spanCount = calculateSpanCount(itemView.context, 64)
            recyclerView.layoutManager = GridLayoutManager(itemView.context, spanCount)
            recyclerView.adapter = ImgAdapter(images)

            // btn_next 클릭 시 DetailImgFragment 이동
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

