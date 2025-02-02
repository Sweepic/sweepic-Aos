package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel

data class SaveImageMemoResponseDto(
    val folderId: Long,
    val folderName: String,
    val imageId: Long,
    val imageUrl: String
) {
    fun toSaveImageMemoResponseModel() =
        SaveImageMemoResponseModel(folderId, folderName, imageId, imageUrl)
}
