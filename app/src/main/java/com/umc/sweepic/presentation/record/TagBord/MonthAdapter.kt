package com.umc.sweepic.presentation.record.TagBord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class MonthAdapter(
    private val months: List<String>,
    private val onMonthSelected: (String) -> Unit
) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private var selectedPosition: Int = -1 // 선택된 월 표시를 위한 변수

    inner class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val monthTextView: TextView = itemView.findViewById(R.id.monthTextView)

        fun bind(month: String, isSelected: Boolean) {
            // 월 텍스트 설정
            monthTextView.text = month

            // 선택 상태에 따라 텍스트 색상 및 스타일 변경
            if (isSelected) {
                monthTextView.setTextColor(itemView.context.getColor(R.color.sweepic))
                monthTextView.setTypeface(null, android.graphics.Typeface.BOLD) // 굵은 글씨
            } else {
                monthTextView.setTextColor(itemView.context.getColor(R.color.gray_line))
                monthTextView.setTypeface(null, android.graphics.Typeface.NORMAL) // 일반 글씨
            }

            // 클릭 이벤트 설정
            itemView.setOnClickListener {
                onMonthSelected(month) // 클릭된 월 전달
                selectedPosition = adapterPosition // 선택된 위치 업데이트
                notifyDataSetChanged() // RecyclerView 갱신
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(months[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = months.size
}
