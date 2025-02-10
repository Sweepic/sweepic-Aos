package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel

data class CreateChallengeUpdateResponseDto(
    val id : String,
    val required : String,
    val remaining : String
){
    fun toCreateChallengeUpdateResponseModel() =
        CreateChallengeUpdateResponseModel(
            id, required, remaining
        )
}
