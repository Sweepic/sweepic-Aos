package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.CreateTextFolderRequestDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface SweepService {
    @GET("memo/list")
    suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto>

    @Multipart
    @POST("memo/text-format/folders")
    suspend fun fetchSweepCreateTextFolder(
        @Part("folder_name") folder_name: RequestBody,
        @Part base64_image: MultipartBody.Part
    ): BaseResponse<CreateTextFolderResponseDto>

    @Multipart
    @PATCH("memo/text-format/folders/{folderId}")
    suspend fun fetchSweepSaveTextMemo(
        @Path("folderId") folderId: Number,
        @Part base64_image: MultipartBody.Part
    ): BaseResponse<CreateTextFolderResponseDto>

    @Multipart
    @POST("memo/image-format/folders")
    suspend fun fetchSweepCreateImageFolder(
        @Part("folderName") folderName: RequestBody,
        @Part image: MultipartBody.Part
    ): BaseResponse<CreateImageFolderResponseDto>

    @Multipart
    @POST("memo/image-format/folders/{folderId}")
    suspend fun fetchSweepSaveImageMemo(
        @Path("folderId") folderId: Long,
        @Part image: MultipartBody.Part
    ): BaseResponse<SaveImageMemoResponseDto>
}