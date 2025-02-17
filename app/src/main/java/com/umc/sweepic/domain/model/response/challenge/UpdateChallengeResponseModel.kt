package com.umc.sweepic.domain.model.response.challenge

data class UpdateChallengeResponseModel(
    val id : String,
    val required : String,
    val remaining : String
)
