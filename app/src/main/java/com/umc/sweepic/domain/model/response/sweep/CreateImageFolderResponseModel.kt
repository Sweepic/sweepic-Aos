package com.umc.sweepic.domain.model.response.sweep

data class CreateImageFolderResponseModel(
    val folderId: Long,
    val folderName: String,
    val imageId: Long,
    val imageUrl: String
)
