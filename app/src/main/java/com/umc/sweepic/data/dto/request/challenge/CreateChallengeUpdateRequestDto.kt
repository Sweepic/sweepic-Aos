package com.umc.sweepic.data.dto.request.challenge

data class CreateChallengeUpdateRequestDto(
    val id : String,
    val required : Int,
    val remaining : Int
)
