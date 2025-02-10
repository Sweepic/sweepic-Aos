package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateChallengeUpdateRequestDto
import java.io.Serializable

data class CreateChallengeUpdateRequestModel(
    val id : String,
    val required : Int,
    val remaining : Int
): Serializable {
    fun toCreateChallengeUpdateRequestDto() =
        CreateChallengeUpdateRequestDto(
            id, required, remaining
        )
}
