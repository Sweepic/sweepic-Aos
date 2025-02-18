package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.DeleteImageRequestDto
import java.io.Serializable

data class DeleteImageRequestModel(
    val imageIds: List<Int>
): Serializable {
    fun toDeleteImageRequestDto() =
        DeleteImageRequestDto(imageIds)
}