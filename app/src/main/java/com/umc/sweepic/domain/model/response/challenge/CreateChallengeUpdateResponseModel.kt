package com.umc.sweepic.domain.model.response.challenge

data class CreateChallengeUpdateResponseModel(
    val id : String,
    val required : String,
    val remaining : String
)
