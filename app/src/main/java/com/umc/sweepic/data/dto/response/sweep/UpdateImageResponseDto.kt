package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.UpdateImageResponseModel

data class UpdateImageResponseDto(
    val imageId: String
) {
    fun toUpdateImageResponseModel() =
        UpdateImageResponseModel(imageId)
}
