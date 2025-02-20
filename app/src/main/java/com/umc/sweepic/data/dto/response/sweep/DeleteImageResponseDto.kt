package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.DeleteImageResponseModel

data class DeleteImageResponseDto(
    val success: Boolean
) {
    fun toDeleteImageResponseModel() =
        DeleteImageResponseModel(success)
}
