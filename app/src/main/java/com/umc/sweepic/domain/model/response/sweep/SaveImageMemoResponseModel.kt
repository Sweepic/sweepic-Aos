package com.umc.sweepic.domain.model.response.sweep

data class SaveImageMemoResponseModel(
    val folderId: Long,
    val folderName: String,
    val imageId: Long,
    val imageUrl: String
)
