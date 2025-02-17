package com.umc.sweepic.data.dto.request.challenge

data class UpdateChallengeRequestDto(
    val remaining : Int,
    val required : Int,
    val id : String
)
