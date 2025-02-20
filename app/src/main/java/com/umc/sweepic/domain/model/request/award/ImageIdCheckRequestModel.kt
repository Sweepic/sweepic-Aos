package com.umc.sweepic.domain.model.request.award

import com.umc.sweepic.data.dto.request.ImageIdCheckRequestDto
import java.io.Serializable

data class ImageIdCheckRequestModel(
    val timestamp: String,
    val mediaId: String
): Serializable{
    fun toImageIdCheckRequestDto() =
        ImageIdCheckRequestDto(
            timestamp, mediaId
        )
}
