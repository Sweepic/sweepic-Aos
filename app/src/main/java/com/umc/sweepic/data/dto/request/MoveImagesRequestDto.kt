package com.umc.sweepic.data.dto.request

data class MoveImagesRequestDto(
    val targetFolderId: String,
    val imageId: List<String>
)