package com.umc.sweepic.data.dto.request

data class MoveImagesRequestDto(
    val targetFolderId: Long,
    val imageId: List<Long>
)