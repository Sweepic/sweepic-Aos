package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.DeleteImagesRequestDto
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.request.UpdateFolderNameRequestDto
import com.umc.sweepic.data.dto.request.UpdateMemoTextRequestDto
import com.umc.sweepic.data.dto.response.DeleteImagesResponseDto
import com.umc.sweepic.data.dto.response.MemoFolderDetailResponseDto
import com.umc.sweepic.data.dto.response.RecordMemoListResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @POST("memo/folders/{folderId}/images/delete")
    suspend fun deleteImages (
        @Path("folderId") folderId: String,
        @Body request: DeleteImagesRequestDto
    ) : BaseResponse<DeleteImagesResponseDto>

    @PATCH("memo/folders/{folderId}/images")
    suspend fun moveImages (
        @Path("folderId") folderId: Long,
        @Body request: MoveImagesRequestDto
    ) : BaseResponse<Unit>

    @PATCH("memo/folders/{folderId}")
    suspend fun updateFolderName(
        @Path("folderId") folderId: String,
        @Body request: UpdateFolderNameRequestDto
    ): BaseResponse<Unit>

    @PATCH("memo/folders/{folderId}/text")
    suspend fun updateMemoText(
        @Path("folderId") folderId: String,
        @Body request: UpdateMemoTextRequestDto
    ) : BaseResponse<Unit>
}