package com.umc.sweepic.data.dto.request.challenge

data class LocationChallengeRequestDto(
    val required: Int,
    val location: String,
    val context: String
)
