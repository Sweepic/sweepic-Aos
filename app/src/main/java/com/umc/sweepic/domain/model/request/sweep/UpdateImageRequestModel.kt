package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import java.io.Serializable

data class UpdateImageRequestModel(
    val timestamp: String,
    val mediaId: String
): Serializable {
    fun toUpdateImageRequestDto() =
        UpdateImageRequestDto(
            timestamp, mediaId
        )
}
