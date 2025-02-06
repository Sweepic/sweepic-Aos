package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto

interface MemoDataSource {
    suspend fun recordMemoList(): BaseResponse<RecordMemoListResponseDto>
    suspend fun searchMemos(keyword: String): BaseResponse<RecordMemoListResponseDto>
    suspend fun fetchMemoFolderDetails(folderId: Long): BaseResponse<MemoFolderDetailResponseDto>
}