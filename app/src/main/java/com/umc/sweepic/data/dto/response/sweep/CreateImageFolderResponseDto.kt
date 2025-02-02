package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel

data class CreateImageFolderResponseDto (
    val folderId: Long,
    val folderName: String,
    val imageId: Long,
    val imageUrl: String
) {
    fun toCreateImageFolderResponseModel() =
        CreateImageFolderResponseModel(
            folderId, folderName, imageId, imageUrl
        )

}