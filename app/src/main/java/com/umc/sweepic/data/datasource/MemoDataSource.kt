package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto

interface MemoDataSource {
    suspend fun recordMemoList(): BaseResponse<RecordMemoListResponseDto>
    suspend fun searchMemos(keyword: String): BaseResponse<RecordMemoListResponseDto>
    suspend fun fetchMemoFolderDetails(folderId: Long): BaseResponse<MemoFolderDetailResponseDto>
    suspend fun deleteMemoFolder(folderId: Long): BaseResponse<Any>
//    suspend fun deleteImages(folderId: Long, imageIds: List<Long>): BaseResponse<Any>
    suspend fun moveImages(folderId: Long, requestDto: MoveImagesRequestDto) : BaseResponse<Unit>
}