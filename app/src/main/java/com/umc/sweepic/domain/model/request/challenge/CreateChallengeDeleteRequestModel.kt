package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateChallengeDeleteRequestDto
import java.io.Serializable

data class CreateChallengeDeleteRequestModel (
    val id : String
): Serializable {
    fun toCreateChallengeDeleteRequestDto() =
        CreateChallengeDeleteRequestDto(
            id
        )
}