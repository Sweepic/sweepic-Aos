package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel

data class CreateLocationChallengeResponseDto (
    val id: String,
    val userId: String,
    val title: String,
    val context: String,
    val requiredCount: Int,
    val remainingCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val acceptedAt: String,
    val completedAt: String,
    val status: Int
) {
    fun toCreateLocationChallengeResponseModel() =
        CreateLocationChallengeResponseModel(
            id, userId, title, context, requiredCount, remainingCount, createdAt, updatedAt, acceptedAt, completedAt, status
        )
}
