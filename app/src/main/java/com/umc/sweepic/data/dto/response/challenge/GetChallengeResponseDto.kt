package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.GetChallengeResponseModel

data class GetChallengeResponseDto(
    val id: String,
    val userId: String,
    val title: String,
    val context: String,
    val challengeLocation: String,
    val challengeDate: String,
    val requiredCount: Int,
    val remainingCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val acceptedAt: String,
    val completedAt: String,
    val status: Int
){
    fun toGetChallengeResponseModel() =
        GetChallengeResponseModel(
            id,
            userId,
            title,
            context,
            challengeLocation,
            challengeDate,
            requiredCount,
            remainingCount,
            createdAt,
            updatedAt,
            acceptedAt,
            completedAt,
            status
        )
}
