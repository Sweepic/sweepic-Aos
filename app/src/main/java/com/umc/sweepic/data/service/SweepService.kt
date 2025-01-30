package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.CreateTextFolderRequestDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SweepService {
    @GET("memo/list")
    suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto>

    @POST("memo/text-format/folders")
    suspend fun fetchSweepCreateTextFolder(
        @Body request: CreateTextFolderRequestDto
    ): BaseResponse<CreateTextFolderResponseDto>

    @Multipart
    @POST("memo/image-format/folders")
    suspend fun fetchSweepCreateImageFolder(
        @Part("folderName") folderName: RequestBody,
        @Part image: MultipartBody.Part
    ): BaseResponse<CreateImageFolderResponseDto>
}