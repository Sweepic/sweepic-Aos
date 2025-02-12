package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.data.dto.response.DeleteImagesResponseDto
import com.umc.sweepic.domain.model.MemoFolderDetailModel
import com.umc.sweepic.domain.model.RecordMemoListModel
import com.umc.sweepic.presentation.record.memo.MemoFolder

interface MemoRepository {
    suspend fun recordMemoList(): Result<RecordMemoListModel>
    suspend fun searchMemos(keyword: String) : Result<RecordMemoListModel>
    suspend fun fetchMemoFolderDetails(folderId: Long): Result<MemoFolderDetailModel>
    suspend fun deleteMemoFolder(folderId: Long): Result<Unit>
//    suspend fun deleteImages(folderId: Long, imageIds: List<Long>): Result<Unit>
    suspend fun moveImages(folderId: Long, targetFolderId: String, imageIds: List<String>): Result<Unit>
    suspend fun deleteImages(folderId: String, imageIds: List<String> ) : Result<DeleteImagesResponseDto>
}
