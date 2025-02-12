package com.umc.sweepic.presentation.record.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.response.GetTagsByDateModel

class HistoryTagAdapter(private var data: Map<Double, List<GetTagsByDateModel.TagsByDateItem>>) :
    RecyclerView.Adapter<HistoryTagAdapter.HistoryTagViewHolder>() {

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
        val month = (12 - position).toDouble()
        holder.monthTextView.text = "${month.toInt()}월"

        val tags = data[month] ?: emptyList()


        val placeTag = tags.find { it.tagCategoryId == "1" }
        val peopleTag = tags.find { it.tagCategoryId == "2" }
        val foodTag = tags.find { it.tagCategoryId == "3" }

        if (placeTag != null) {
            holder.placeTag.text = "#${placeTag.content}"
            holder.placeTagDesc.text = "에 총 ${placeTag.count}번 방문했어요"
        } else {
            holder.placeTag.text = "데이터 없음"
            holder.placeTagDesc.text = ""
        }

        if (peopleTag != null) {
            holder.peopleTag.text = "#${peopleTag.content}"
            holder.peopleTagDesc.text = "와 총 ${peopleTag.count}번 만났어요"
        } else {
            holder.peopleTag.text = "데이터 없음"
            holder.peopleTagDesc.text = ""
        }

        if (foodTag != null) {
            holder.foodTag.text = "#${foodTag.content}"
            holder.foodTagDesc.text = "를 총 ${foodTag.count}번 먹었어요"
        } else {
            holder.foodTag.text = "데이터 없음"
            holder.foodTagDesc.text = ""
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    fun updateData(newData: Map<Double, List<GetTagsByDateModel.TagsByDateItem>>) {
        data = newData
        notifyDataSetChanged()
    }
}
