package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.MemoDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto
import com.umc.sweepic.data.service.MemoService
import javax.inject.Inject

class MemoDataSourceImpl @Inject constructor(
    private val memoService: MemoService
): MemoDataSource {
    override suspend fun recordMemoList(): BaseResponse<RecordMemoListResponseDto>
    = memoService.recordMemoList()

    override suspend fun searchMemos(keyword: String): BaseResponse<RecordMemoListResponseDto>
        = memoService.searchMemos(keyword)

    override suspend fun fetchMemoFolderDetails(folderId: Long): BaseResponse<MemoFolderDetailResponseDto>
    = memoService.fetchMemoFolderDetails(folderId)

    override suspend fun deleteMemoFolder(folderId: Long): BaseResponse<Any> =
        memoService.deleteMemoFolder(folderId)

    override suspend fun moveImages(folderId: Long, requestDto: MoveImagesRequestDto) : BaseResponse<Unit>
    = memoService.moveImages(folderId, requestDto)
    }



