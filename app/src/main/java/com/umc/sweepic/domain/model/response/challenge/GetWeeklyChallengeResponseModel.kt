package com.umc.sweepic.domain.model.response.challenge

import com.umc.sweepic.presentation.challenge.adapter.Challenge

data class GetWeeklyChallengeResponseModel(
    val id : String,
    val userId: String,
    val title : String,
    val context : String,
    val challengeDate: String?,
    val requiredCount : Int,
    val remainingCount : Int,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String?,
    val completedAt : String?,
    val status : Int,
)

fun GetWeeklyChallengeResponseModel.toChallenge(): Challenge {
    return Challenge(
        id = this.id,
        title = this.title,
        context = this.context,
        challengeLocation = null, // 위치 기반 챌린지가 아니므로 null
        challengeDate = this.challengeDate, // 날짜 기반 챌린지가 아니므로 null
        requiredCount = this.requiredCount,  // ✅ 필드명 수정 (required → requiredCount)
        remainingCount = this.remainingCount,
        userId = this.userId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        acceptedAt = this.acceptedAt,
        completedAt = this.completedAt,
        status = this.status,
        photos = listOf()// 필요하면 추가 가능
    )
}