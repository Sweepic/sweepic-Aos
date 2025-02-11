package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import java.io.Serializable

data class CreateWeeklyChallengeRequestModel(
    val context : String,
    val challengeDate : String,
    val required : Int
): Serializable {
    fun toCreateWeeklyChallengeRequestDto() =
        CreateWeeklyChallengeRequestDto(
            context, challengeDate, required
        )
}
