package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.DeleteImagesRequestDto
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.request.UpdateFolderNameRequestDto
import com.umc.sweepic.data.dto.request.UpdateMemoTextRequestDto
import com.umc.sweepic.data.dto.response.DeleteImagesResponseDto
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto

interface MemoDataSource {
    suspend fun recordMemoList(): BaseResponse<RecordMemoListResponseDto>
    suspend fun searchMemos(keyword: String): BaseResponse<RecordMemoListResponseDto>
    suspend fun fetchMemoFolderDetails(folderId: Long): BaseResponse<MemoFolderDetailResponseDto>
    suspend fun deleteMemoFolder(folderId: Long): BaseResponse<Any>
    suspend fun deleteImages(folderId: String, requestDto: DeleteImagesRequestDto): BaseResponse<DeleteImagesResponseDto>
    suspend fun moveImages(folderId: Long, requestDto: MoveImagesRequestDto) : BaseResponse<Unit>
    suspend fun updateFolderName(folderId: String, requestDto: UpdateFolderNameRequestDto) : BaseResponse<Unit>
    suspend fun updateMemoText(folderId: String, requestDto: UpdateMemoTextRequestDto) : BaseResponse<Unit>
}