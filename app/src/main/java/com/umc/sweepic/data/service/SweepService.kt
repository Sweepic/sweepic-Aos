package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.sweep.DeleteImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.MoveTrashRequestDto
import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.DeleteImageResponseDto
import com.umc.sweepic.data.dto.response.sweep.MoveTrashResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import com.umc.sweepic.data.dto.response.sweep.UpdateImageResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface SweepService {
    // 메모 관련 API
    @GET("memo/list")
    suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto>

    @Multipart
    @POST("memo/text-format/folders")
    suspend fun fetchSweepCreateTextFolder(
        @Part("folder_name") folderName: RequestBody,
        @Part base64_image: MultipartBody.Part
    ): BaseResponse<CreateTextFolderResponseDto>

    @Multipart
    @PATCH("memo/text-format/folders/{folderId}")
    suspend fun fetchSweepSaveTextMemo(
        @Path("folderId") folderId: Long,
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

    // 휴지통 관련 API
    @DELETE("trust")
    suspend fun fetchSweepDeleteImage(
        @Body request: DeleteImageRequestDto
    ): BaseResponse<DeleteImageResponseDto>

    @PATCH("trust/active")
    suspend fun fetchSweepMoveToTrash(
        @Body request: MoveTrashRequestDto
    ): BaseResponse<MoveTrashResponseDto>

    // 이미지 정보 넘기기
    @POST("images")
    suspend fun fetchSweepImages(
        @Body request: UpdateImageRequestDto
    ): BaseResponse<UpdateImageResponseDto>

}