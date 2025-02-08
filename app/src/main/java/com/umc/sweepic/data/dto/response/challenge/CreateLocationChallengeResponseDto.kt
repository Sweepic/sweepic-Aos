package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel

data class CreateLocationChallengeResponseDto (
    val id: String,
    val title: String,
    val context: Double,
    val requireCount: Int,
    val remainingCount: Int,
    val userId: String,
    val createAt: String,
    val updateAt: String,
    val acceptedAt: String,
    val completedAt: String,
    val status: Int
) {
    fun toCreateLocationChallengeResponseModel() =
        CreateLocationChallengeResponseModel(
            id, title, context, requireCount, remainingCount, userId, createAt, updateAt, acceptedAt, completedAt, status
        )
}
