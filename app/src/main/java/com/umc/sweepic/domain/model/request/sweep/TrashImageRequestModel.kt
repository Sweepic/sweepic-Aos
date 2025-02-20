package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.TrashImageRequestDto
import java.io.Serializable

data class TrashImageRequestModel(
    val mediaIdList: List<String>
): Serializable {
    fun toTrashImageRequestDto() =
        TrashImageRequestDto(mediaIdList)
}
