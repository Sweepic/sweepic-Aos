package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.award.ImageIdCheckResponseModel

data class ImageIdCheckResponseDto(
    val imageId: String
) {
    fun toImageIdCheckResponseModel() =
        ImageIdCheckResponseModel(imageId)
}
