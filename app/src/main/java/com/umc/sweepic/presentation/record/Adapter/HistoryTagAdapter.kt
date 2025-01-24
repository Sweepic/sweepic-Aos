package com.umc.sweepic.presentation.record.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class HistoryTagAdapter(private val data: List<String>) :
    RecyclerView.Adapter<HistoryTagAdapter.HistoryTagViewHolder>() {

    class HistoryTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthTextView: TextView = itemView.findViewById(R.id.tv_record_month)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTagViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_tag, parent, false)
        return HistoryTagViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryTagViewHolder, position: Int) {
        // "월" 텍스트만 바꾸기
        holder.monthTextView.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size // 데이터 크기만큼 아이템 표시
    }
}
