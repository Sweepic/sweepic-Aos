package com.umc.sweepic.data.dto.request.challenge

data class WeekChallengeRequestDto(
    val required: Int,
    val challengeDate: String,
    val context: String
)
