package com.umc.sweepic.data.dto.request.challenge

data class CreateWeeklyChallengeRequestDto(
    val context : String,
    val challengeDate : String,
    val required : Int
)
