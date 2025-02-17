package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import java.io.Serializable

data class CreateLocationChallengeRequestModel(
    val required: Int,
    val location: String,
    val context: String
): Serializable{
    fun toCreateLocationChallengeRequestDto() =
        CreateLocationChallengeRequestDto(
            required, location, context
        )
}