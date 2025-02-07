package com.umc.sweepic.presentation.record.tagboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class DateRecyclerAdapter(
    private val dates: List<String>,
    private val onDateSelected: (String) -> Unit
) : RecyclerView.Adapter<DateRecyclerAdapter.DateViewHolder>() {

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(date: String) {
            dateText.text = date
            itemView.setOnClickListener {
                onDateSelected(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount(): Int = dates.size
}