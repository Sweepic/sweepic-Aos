package com.umc.sweepic.domain.model.response.sweep

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoFolderDetailModel(
    val folderId: Long,
    val folderName: String,
    val imageText: String?,
    val images: List<ImageModel>
) : Parcelable {
    @Parcelize
    data class ImageModel(
        val imageId: Long,
        val imageUrl: String
    ) : Parcelable
}
