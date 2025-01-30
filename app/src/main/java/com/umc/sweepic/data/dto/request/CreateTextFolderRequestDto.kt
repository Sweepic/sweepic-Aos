package com.umc.sweepic.data.dto.request

data class CreateTextFolderRequestDto(
    val base64Image: String,
    val userId: Long,
    val folderName: String
)