package com.umc.sweepic.presentation.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R

class TagBoardRVA(private var items: List<String>) :
    RecyclerView.Adapter<TagBoardRVA.TagBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagBoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tagbord, parent, false)
        return TagBoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagBoardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // 데이터 업데이트
    fun updateData(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }

    class TagBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(item: String) {
            dateTextView.text = item
        }
    }
}
