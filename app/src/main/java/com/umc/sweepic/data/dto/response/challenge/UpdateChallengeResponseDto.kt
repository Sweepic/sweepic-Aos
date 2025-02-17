package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.UpdateChallengeResponseModel

data class UpdateChallengeResponseDto(
    val id : String,
    val required : String,
    val remaining : String
){
    fun toUpdateChallengeResponseModel() =
        UpdateChallengeResponseModel(
            id, required, remaining
        )
}
