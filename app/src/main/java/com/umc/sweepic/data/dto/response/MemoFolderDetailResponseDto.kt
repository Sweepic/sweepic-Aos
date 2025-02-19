package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.sweep.MemoFolderDetailModel

data class MemoFolderDetailResponseDto(
    val folderId: Long,
    val folderName: String,
    val imageText: String?,
    val images: List<ImageResponseDto>? // ✅ null 허용하도록 수정
) {
    data class ImageResponseDto(
        val imageId: Long,
        val imageUrl: String
    )

    // ✅ Model로 변환하는 함수 (null 방어 코드 추가)
    fun toMemoFolderDetailModel(): MemoFolderDetailModel {
        return MemoFolderDetailModel(
            folderId = folderId,
            folderName = folderName,
            imageText = imageText,
            images = images?.map { MemoFolderDetailModel.ImageModel(it.imageId, it.imageUrl) } ?: emptyList() // ✅ null 방지
        )
    }
}
