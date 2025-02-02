package com.umc.sweepic.presentation.record.tagboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.umc.sweepic.R

class ChipAdapter(
    private val chipList: List<String>,
    private val isDetail: Boolean,
    private val onTagClick: (String) -> Unit
) : RecyclerView.Adapter<ChipAdapter.ChipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val layoutId = if (isDetail) R.layout.item_detail_chip else R.layout.item_chip
        val chip = LayoutInflater.from(parent.context).inflate(layoutId, parent, false) as Chip
        return ChipViewHolder(chip,onTagClick)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.bind(chipList[position])
    }

    override fun getItemCount(): Int = chipList.size

    class ChipViewHolder(private val chip: Chip, private val  onTagClick: (String) -> Unit) : RecyclerView.ViewHolder(chip) {
        fun bind(tag: String) {
            chip.text = tag
            chip.setOnClickListener {
                onTagClick(tag) // 클릭 시 해당 태그를 전달
            }
        }
    }
}
