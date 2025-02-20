package com.umc.sweepic.domain.model.response.challenge

data class GoalChallengeResponseModel(
    val id: String,
    val userId: String,
    val requiredCount: Int,
    val remainingCount: Int,
    val updatedAt: String,
    val status: Int
)
