package com.umc.sweepic.domain.model.response.challenge

data class CommonChallengeResponseModel(
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
)
