package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel

data class SweepMemoListResponseDto (
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
            SweepMemoListModel.MemoFolderModel(folderId, folderName, imageText, imageCount, firstImageId, firstImageUrl, createdAt)
    }
    fun toSweepMemoListModel() =
        SweepMemoListModel(data.map { it.toMemoFolderModel() })
}