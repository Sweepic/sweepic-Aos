package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel

data class ChallengeGetResponseDto(
    val id: String,
    val title: String,
    val context: String,
    val challengeLocation: String,
    val challengeDate: String,
    val requiredCount: Int,
    val remainingCount: Int,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val acceptedAt: String,
    val completedAt: String,
    val status: Int
){
    fun toChallengeGetResponseModel() =
        ChallengeGetResponseModel(
            id, title, context, challengeDate, challengeDate, requiredCount,remainingCount, userId, createdAt, updatedAt, acceptedAt, completedAt, status
        )
}
