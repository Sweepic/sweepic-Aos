package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel

data class CreateTextFolderResponseDto(
    val folder_id: Long?,
    val image_text: String?
) {
    fun toCreateTextFolderResponseModel() =
        CreateTextFolderResponseModel(folder_id, image_text)
}