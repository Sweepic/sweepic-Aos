package com.umc.sweepic.presentation.challenge.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.R

class ChallengeAdapter(private val onChallengeAccept: (String) -> Unit) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private var challenges: List<Challenge> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.bind(challenge)

        holder.itemView.findViewById<Button>(R.id.btn_challenge_start).setOnClickListener {
            if (challenge.status == 1) { // ✅ 새로운 챌린지만 수락 가능
                Log.d("ChallengeAdapter", "🚀 챌린지 수락 요청: ${challenge.id}")
                onChallengeAccept(challenge.id)
            } else {
                Log.e("ChallengeAdapter", "❌ 챌린지가 이미 진행 중이거나 완료됨: ${challenge.id}")
            }
        }
    }


    override fun getItemCount(): Int = challenges.size

    fun setChallenges(newChallenges: List<Challenge>) {
        Log.e("ChallengeAdapter", "🔥 RecyclerView에 들어갈 챌린지 리스트: $newChallenges")
        challenges = newChallenges.toList()
        notifyDataSetChanged()
    }


    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val challengeTitle: TextView = itemView.findViewById(R.id.tv_challenge_title)
        private val challengeNumPhoto: TextView = itemView.findViewById(R.id.tv_numphoto)
        private val challengeImage1: ImageView = itemView.findViewById(R.id.iv_challenge_img1)
        private val challengeImage2: ImageView = itemView.findViewById(R.id.iv_challenge_img2)
        private val challengeImage3: ImageView = itemView.findViewById(R.id.iv_challenge_img3)

        fun bind(challenge: Challenge) {
            challengeTitle.text = challenge.title
            challengeNumPhoto.text = "${challenge.requiredCount} 장"

            val photos = challenge.photos
            val context = itemView.context
            val placeholder = R.drawable.img_test // 기본 이미지

            Log.e("ChallengeAdapter", "🔥 챌린지 ${challenge.title} - 사진 개수: ${photos.size}, 사진 목록: $photos")

            if (photos.isEmpty()) {
                Log.e("ChallengeAdapter", "❌ photos 리스트가 비어 있음! SharedPreferences에서 데이터가 안 넘어왔을 가능성 있음")
            }


            // 📌 첫 번째, 두 번째, 세 번째 사진 로딩
            loadImage(context, challengeImage1, photos.getOrNull(0), placeholder)
            loadImage(context, challengeImage2, photos.getOrNull(1), placeholder)
            loadImage(context, challengeImage3, photos.getOrNull(2), placeholder)
        }

        /*        private fun loadImage(context: Context, imageView: ImageView, imageUrl: String?, placeholder: Int) {
            if (imageUrl.isNullOrEmpty()) {
                Log.e("ChallengeAdapter", "❌ 이미지 URL이 null 또는 빈 문자열: $imageUrl")
                imageView.setImageResource(placeholder)
                imageView.visibility = View.GONE
                return
            }

            val validImagePath = if (imageUrl.startsWith("file://")) imageUrl else "file://$imageUrl"

            Log.e("ChallengeAdapter", "📸 Glide 로드 시도: $validImagePath") // 🔥 확인용 로그 추가

            Glide.with(context)
                .load(validImagePath)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView)

            imageView.visibility = View.VISIBLE // ✅ 이미지 있으면 보이게 하기
        }
    }*/

        private fun loadImage(
            context: Context,
            imageView: ImageView,
            imageUrl: String?,
            placeholder: Int
        ) {
            Glide.with(context)
                .load(imageUrl?.let { "file://$it" } ?: placeholder)
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView)
        }
    }
}
