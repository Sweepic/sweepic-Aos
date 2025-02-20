package com.umc.sweepic.presentation.record.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel
import java.util.*

class HistoryTagAdapter(
    private var data: Map<Double, List<GetMostTaggedModel.MostTaggedItem>>,
    private var selectedYear: Double
) : RecyclerView.Adapter<HistoryTagAdapter.HistoryTagViewHolder>() {

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR).toDouble()
    private val currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1).toDouble()


    class HistoryTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthTextView: TextView = itemView.findViewById(R.id.tv_record_month)

        val placeTag: TextView = itemView.findViewById(R.id.tv_tag_place)
        val placeTagDesc: TextView = itemView.findViewById(R.id.tv_tag_place_ds)

        val peopleTag: TextView = itemView.findViewById(R.id.tv_tag_people)
        val peopleTagDesc: TextView = itemView.findViewById(R.id.tv_tag_people_ds)

        val foodTag: TextView = itemView.findViewById(R.id.tv_tag_food)
        val foodTagDesc: TextView = itemView.findViewById(R.id.tv_tag_food_ds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTagViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_tag, parent, false)
        return HistoryTagViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryTagViewHolder, position: Int) {
        val month = if (selectedYear == currentYear) {
            (currentMonth - position).coerceAtLeast(1.0) // 현재 월부터 1월까지 역순으로 표시되어야됨
        } else {
            (12 - position).toDouble()
        }

        holder.monthTextView.text = "${month.toInt()}월"
        val tags = data[month] ?: emptyList()

        val placeTag = tags.find { it.tagCategoryId == "1" }
        val peopleTag = tags.find { it.tagCategoryId == "2" }
        val foodTag = tags.find { it.tagCategoryId == "3" }

        holder.placeTag.text = placeTag?.let { "#${it.content}" } ?: "태그 없음"
        holder.placeTagDesc.text = placeTag?.let { "에 총 ${it._count._all}번 방문했어요" } ?: ""

        holder.peopleTag.text = peopleTag?.let { "#${it.content}" } ?: "태그 없음"
        holder.peopleTagDesc.text = peopleTag?.let { "와 총 ${it._count._all}번 만났어요" } ?: ""

        holder.foodTag.text = foodTag?.let { "#${it.content}" } ?: "태그 없음"
        holder.foodTagDesc.text = foodTag?.let { "를 총 ${it._count._all}번 먹었어요" } ?: ""
    }

    override fun getItemCount(): Int {
        return if (selectedYear == currentYear) {
            currentMonth.toInt() // 현재 년도면 현재 월부터
        } else {
            12
        }
    }

    fun updateData(newData: Map<Double, List<GetMostTaggedModel.MostTaggedItem>>, newYear: Double) {
        data = newData
        selectedYear = newYear
        notifyDataSetChanged()
    }
}
