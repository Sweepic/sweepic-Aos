package com.umc.sweepic.presentation.record.TagBord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.umc.sweepic.R

class ChipAdapter(private val chipList: List<String>) :
    RecyclerView.Adapter<ChipAdapter.ChipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val chip = LayoutInflater.from(parent.context).inflate(R.layout.item_chip, parent, false) as Chip
        return ChipViewHolder(chip)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(chipList[position])
    }

    override fun getItemCount(): Int = chipList.size

    class ChipViewHolder(private val chip: Chip) : RecyclerView.ViewHolder(chip) {
        fun bind(tag: String) {
            chip.text = tag
        }
    }
}
