package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import java.io.Serializable

data class CreateWeeklyChallengeRequestModel(
    val required : Int,
    val challengeDate : String,
    val context : String
): Serializable {
    fun toCreateWeeklyChallengeRequestDto() =
        CreateWeeklyChallengeRequestDto(
            required, challengeDate, context
        )
}
