package com.umc.sweepic.data.dto.request.sweep

data class CreateTextFolderRequestDto(
    val base64_image: String,
    val user_id: Long,
    val folder_name: String
)