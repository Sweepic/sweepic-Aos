package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateChallengeDeleteResponseModel

data class CreateChallengeDeleteResponseDto(
    val id : String
){
    fun toCreateChallengeDeleteResponseModel() =
        CreateChallengeDeleteResponseModel(
            id
        )
}
