package com.umc.sweepic.data.dto.response

data class DeleteImagesResponseDto(
    val folderId: String,
    val folderName: String,
    val imageText: String,
    val images: List<ImageDetail>
)

data class ImageDetail(
    val imageId: String,
    val imageUrl: String
)
