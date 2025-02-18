package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.GoalChallengeResponseModel

data class GoalChallengeResponseDto(
    val id: String,
    val userId: String,
    val requiredCount: Int,
    val remainingCount: Int,
    val updatedAt: String,
    val status: Int
){
    fun toGoalChallengeResponseModel() =
        GoalChallengeResponseModel(id, userId, requiredCount, remainingCount, updatedAt, status)
}
