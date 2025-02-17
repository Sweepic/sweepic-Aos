package com.umc.sweepic.data.dto.request.challenge

data class CreateLocationChallengeRequestDto(
    val required: Int,
    val location: String,
    val context: String
)
