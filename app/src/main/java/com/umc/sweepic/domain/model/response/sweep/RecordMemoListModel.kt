package com.umc.sweepic.domain.model.response.sweep

data class RecordMemoListModel(
    val data: List<MemoFolderModel>
) {
    data class MemoFolderModel(
        val folderId: Long,
        val folderName: String,
        val imageText: String,
        val imageCount: Int,
        val firstImageId: Long?,
        val firstImageUrl: String?,
        val createdAt: String?
    )
}
