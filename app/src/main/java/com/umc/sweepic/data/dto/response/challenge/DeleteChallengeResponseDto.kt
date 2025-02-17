package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.DeleteChallengeResponseModel

data class DeleteChallengeResponseDto(
    val id : String
){
    fun toDeleteChallengeResponseModel() =
        DeleteChallengeResponseModel(
            id
        )
}
