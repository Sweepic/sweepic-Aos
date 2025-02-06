package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.MemoFolderDetailModel

data class MemoFolderDetailResponseDto(
    val folderId: Long,
    val folderName: String,
    val imageText: String?,
    val images: List<ImageResponseDto>
) {
    data class ImageResponseDto(
        val imageId: Long,
        val imageUrl: String
    )

    // ✅ Model로 변환하는 함수 추가
    fun toMemoFolderDetailModel(): MemoFolderDetailModel {
        return MemoFolderDetailModel(
            folderId = folderId,
            folderName = folderName,
            imageText = imageText,
            images = images.map { MemoFolderDetailModel.ImageModel(it.imageId, it.imageUrl) }
        )
    }
}