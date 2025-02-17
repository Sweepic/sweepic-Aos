package com.umc.sweepic.data.dto.request.challenge

data class CreateWeeklyChallengeRequestDto(
    val required : Int,
    val challengeDate : String,
    val context : String
)
