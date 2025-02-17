package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.UpdateChallengeRequestDto
import java.io.Serializable

data class UpdateChallengeRequestModel(
    val remaining : Int,
    val required : Int,
    val id : String
): Serializable {
    fun toUpdateChallengeRequestDto() =
        UpdateChallengeRequestDto(
            remaining, required, id
        )
}
