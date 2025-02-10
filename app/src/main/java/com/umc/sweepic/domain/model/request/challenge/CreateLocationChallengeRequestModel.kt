package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import java.io.Serializable

data class CreateLocationChallengeRequestModel(
    val userId: String,
    val title: String,
    val context: String,
    val location: String,
    val required: Int
): Serializable{
    fun toCreateLocationChallengeRequestDto() =
        CreateLocationChallengeRequestDto(
            userId, title, context, location, required
        )
}