package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MemoService {
    @GET("memo/list")
    suspend fun recordMemoList(): BaseResponse<RecordMemoListResponseDto>

    @GET("memo/search")
    suspend fun searchMemos(
        @Query("keyword") keyword:String
    ): BaseResponse<RecordMemoListResponseDto>

    @GET("memo/folders/{folderId}")
    suspend fun fetchMemoFolderDetails(
        @Path("folderId") folderId: Long
    ): BaseResponse<MemoFolderDetailResponseDto>

    @DELETE("memo/folders/{folderId}")
    suspend fun deleteMemoFolder (
        @Path("folderId") folderId: Long
    ): BaseResponse<Any>

//    @DELETE("memo/folders/{folderId}/images")
//    suspend fun deleteImages (
//        @Path("folderId") folderId: Long,
//        @Query("imageIds") imageIds: List<Long>
//    ) : BaseResponse<Any>

    @PATCH("memo/folders/{folderId}/images/move")
    suspend fun moveImages (
        @Path("folderId") folderId: Long,
        @Body request: MoveImagesRequestDto
    ) : BaseResponse<Unit>
}