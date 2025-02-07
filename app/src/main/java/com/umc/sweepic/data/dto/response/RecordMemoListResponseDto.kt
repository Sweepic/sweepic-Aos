package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.RecordMemoListModel


data class RecordMemoListResponseDto (
    val data: List<MemoFolderResponseDto>
){
    data class MemoFolderResponseDto(
        val folderId: Long,
        val folderName: String,
        val imageText: String,
        val imageCount: Int,
        val firstImageId: Long,
        val firstImageUrl: String,
        val createdAt: String
    ) {
        fun toMemoFolderModel() =
            RecordMemoListModel.MemoFolderModel(folderId, folderName, imageText, imageCount, firstImageId, firstImageUrl, createdAt)
    }
    fun toRecordMemoListModel() =
        RecordMemoListModel(data.map { it.toMemoFolderModel() })
}