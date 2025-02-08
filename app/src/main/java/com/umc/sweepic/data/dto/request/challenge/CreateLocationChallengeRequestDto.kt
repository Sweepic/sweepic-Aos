package com.umc.sweepic.data.dto.request.challenge

data class CreateLocationChallengeRequestDto(
    val userId: String,
    val title: String,
    val context: String,
    val location: String,
    val required: Int
)
