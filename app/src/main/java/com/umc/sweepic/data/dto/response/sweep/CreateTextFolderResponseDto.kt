package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel

data class CreateTextFolderResponseDto(
    val folderId: Long,
    val imageText: String
) {
    fun toCreateTextFolderResponseModel() =
        CreateTextFolderResponseModel(folderId, imageText)
}