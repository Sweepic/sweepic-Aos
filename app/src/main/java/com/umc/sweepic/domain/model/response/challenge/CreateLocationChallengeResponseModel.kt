package com.umc.sweepic.domain.model.response.challenge

data class CreateLocationChallengeResponseModel(
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
)