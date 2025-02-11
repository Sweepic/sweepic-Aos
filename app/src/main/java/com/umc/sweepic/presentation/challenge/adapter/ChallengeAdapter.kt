package com.umc.sweepic.presentation.challenge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.Challenge

class ChallengeAdapter(
    private val onChallengeButtonClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private val challengeList = mutableListOf<Challenge>() // MutableList로 변경

    // 데이터 설정 (중복 방지)
    fun setChallenges(challenges: List<Challenge>) {
        challengeList.clear() // 기존 데이터 초기화
        challengeList.addAll(challenges) // 새 데이터 추가
        notifyDataSetChanged() // 전체 업데이트
    }

    inner class ChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numPhoto: TextView = view.findViewById(R.id.tv_numphoto)
        val title: TextView = view.findViewById(R.id.tv_challenge_title)
        val image: ImageView = view.findViewById(R.id.iv_challenge_img)
        val button: Button = view.findViewById(R.id.btn_challenge_start)

        // 데이터 바인딩
        fun bind(challenge: Challenge) {
            numPhoto.text = challenge.num_photo
            title.text = challenge.title
            image.setImageResource(challenge.imageResId)

            button.setOnClickListener {
                onChallengeButtonClick(challenge) // 버튼 클릭 이벤트 콜백 호출c
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vp, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(challengeList[position]) // 데이터 바인딩 호출
    }

    override fun getItemCount(): Int = challengeList.size
}