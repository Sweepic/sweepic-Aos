package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.data.dto.response.DeleteImagesResponseDto
import com.umc.sweepic.domain.model.response.sweep.MemoFolderDetailModel
import com.umc.sweepic.domain.model.response.sweep.RecordMemoListModel

interface MemoRepository {
    suspend fun recordMemoList(): Result<RecordMemoListModel>
    suspend fun searchMemos(keyword: String) : Result<RecordMemoListModel>
    suspend fun fetchMemoFolderDetails(folderId: Long): Result<MemoFolderDetailModel>
    suspend fun deleteMemoFolder(folderId: Long): Result<Unit>
    suspend fun moveImages(folderId: Long, targetFolderId: String, imageIds: List<String>): Result<Unit>
    suspend fun deleteImages(folderId: String, imageIds: List<String> ) : Result<DeleteImagesResponseDto>
    suspend fun updateFolderName(folderId: String,newName: String):Result<Unit>
    suspend fun updateMemoText(folderId: String, newText: String) : Result<Unit>
}
