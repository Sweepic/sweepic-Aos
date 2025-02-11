package com.umc.sweepic.data.dto.request.challenge

data class CreateLocationChallengeRequestDto(
    val context: String,
    val location: String,
    val required: Int
)
