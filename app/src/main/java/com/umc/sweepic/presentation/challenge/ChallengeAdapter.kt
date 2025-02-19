package com.umc.sweepic.presentation.challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemVpBinding

class ChallengeAdapter(
    private val onAcceptChallenge: (String) -> Unit // 챌린지 수락 콜백 추가
) : ListAdapter<ChallengeWithImages, ChallengeAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemVpBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChallengeWithImages) {
            val challenge = item.challenge

            binding.tvNumphoto.text = "${challenge.requiredCount}장"
            binding.tvChallengeTitle.text = challenge.title.substring(3)

            val images = item.images
            Glide.with(binding.ivChallengeImg.context).load(images.getOrNull(0)?.uri).into(binding.ivChallengeImg)
            Glide.with(binding.ivChallengeImg2.context).load(images.getOrNull(1)?.uri).into(binding.ivChallengeImg2)
            Glide.with(binding.ivChallengeImg3.context).load(images.getOrNull(2)?.uri).into(binding.ivChallengeImg3)

            binding.btnChallengeStart.setOnClickListener {
                onAcceptChallenge(challenge.id) // challenge.id를 사용하여 API 호출
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChallengeWithImages>() {
        override fun areItemsTheSame(oldItem: ChallengeWithImages, newItem: ChallengeWithImages): Boolean {
            return oldItem.challenge.id == newItem.challenge.id
        }

        override fun areContentsTheSame(oldItem: ChallengeWithImages, newItem: ChallengeWithImages): Boolean {
            return oldItem == newItem
        }
    }
}
