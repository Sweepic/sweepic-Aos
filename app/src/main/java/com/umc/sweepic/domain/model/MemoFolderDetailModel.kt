package com.umc.sweepic.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoFolderDetailModel(
    val folderId: Long,
    val folderName: String,
    val imageText: String?,
    val images: List<ImageModel> // ✅ API에서 받은 실제 이미지 URL 리스트
) : Parcelable {
    @Parcelize
    data class ImageModel(
        val imageId: Long,
        val imageUrl: String // ✅ 여기서 URL을 받음
    ) : Parcelable
}
