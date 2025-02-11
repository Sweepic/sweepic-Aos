package com.umc.sweepic.domain.model.response.challenge

data class ChallengeGetResponseModel(
    val id: String,
    val title: String,
    val context: String,
    val challengeLocation: String?,
    val challengeDate: String?,
    val requiredCount: Int,
    val remainingCount: Int,
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val acceptedAt: String?,
    val completedAt: String?,
    val status: Int
)
