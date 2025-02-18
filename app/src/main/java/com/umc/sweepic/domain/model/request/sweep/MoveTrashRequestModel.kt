package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.MoveTrashRequestDto
import java.io.Serializable

data class MoveTrashRequestModel(
    val mediaId: Int
): Serializable {
    fun toMoveTrashRequestDto() =
        MoveTrashRequestDto(mediaId)
}
