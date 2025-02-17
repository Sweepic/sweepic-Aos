package com.umc.sweepic.domain.model.response.challenge

import com.umc.sweepic.presentation.challenge.adapter.Challenge

data class GetLocationChallengeResponseModel(
    val id : String,
    val userId: String,
    val title : String,
    val context : String,
    val requiredCount : Int,
    val remainingCount : Int,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String?,
    val completedAt : String?,
    val status : Int
)
fun GetLocationChallengeResponseModel.toChallenge(): Challenge {
    return Challenge(
        id = this.id,
        title = this.title,
        context = this.context,
        challengeLocation = null, // 🔥 위치 정보 필요 없음
        challengeDate = null, // 🔥 주간 챌린지와 다르게 날짜가 없음
        requiredCount = this.requiredCount,
        remainingCount = this.remainingCount,
        userId = this.userId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        acceptedAt = this.acceptedAt,
        completedAt = this.completedAt,
        status = this.status,
        photos = listOf() // 📌 필요하면 추가 가능
    )
}
