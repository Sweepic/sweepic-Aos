package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.MoveTrashResponseModel

data class MoveTrashResponseDto(
    val mediaId: Int,
    val status: Int
) {
    fun toMoveTrashResponseModel() =
        MoveTrashResponseModel(mediaId, status)
}
