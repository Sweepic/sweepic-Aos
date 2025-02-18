package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.award.CreateAwardResponseModel

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

data class CreateAwardResponseDto(
    val id: String,
    val awardMonth: String,
    val createdAt: String,
    val updateAt: String,
    val userId: String
){
    fun toCreateAwardResponseModel() =
        CreateAwardResponseModel(
            id, awardMonth, createdAt, updateAt, userId
        )
}