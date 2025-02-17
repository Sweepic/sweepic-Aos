package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.GetUserChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationLogicResponseModel

data class GetUserChallengeResponseDto(
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
    fun toGetUserChallengeResponseModel() =
        GetUserChallengeResponseModel(
            id, title, context, challengeDate, challengeDate, requiredCount,remainingCount, userId, createdAt, updatedAt, acceptedAt, completedAt, status
        )

    companion object {
        fun List<GetUserChallengeResponseDto>.toResponseModelList(): List<GetUserChallengeResponseModel> {
            return this.map { it.toGetUserChallengeResponseModel() }
        }
    }
}
