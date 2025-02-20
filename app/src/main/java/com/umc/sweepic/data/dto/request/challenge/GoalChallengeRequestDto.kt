package com.umc.sweepic.data.dto.request.challenge

data class GoalChallengeRequestDto(
    val remaining: Int,
    val required: Int,
    val id: String
)
