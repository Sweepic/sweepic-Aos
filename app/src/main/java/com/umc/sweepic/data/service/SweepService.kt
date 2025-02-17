package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.sweep.CreateMemoFolderRequestDto
import com.umc.sweepic.data.dto.request.sweep.DeleteImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.MoveTrashRequestDto
import com.umc.sweepic.data.dto.request.sweep.TagRequestDto
import com.umc.sweepic.data.dto.request.sweep.TrashImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import com.umc.sweepic.data.dto.response.sweep.AiTagResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateMemoFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.DeleteImageResponseDto
import com.umc.sweepic.data.dto.response.sweep.MoveTrashResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagResponseDto
import com.umc.sweepic.data.dto.response.sweep.UpdateImageResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @POST("memo/folders")
    suspend fun fetchSweepCreateMemoFolder(
        @Body request: CreateMemoFolderRequestDto
    ): BaseResponse<CreateMemoFolderResponseDto>

    // 휴지통 관련 API
    @PATCH("trash/images/{imageId}")
    suspend fun fetchMoveImageToTrash(
        @Path("imageId") imageId: String
    ): BaseResponse<String>

    @PATCH("trash/active")
    suspend fun fetchRestoreTrashImage(
        @Body request: TrashImageRequestDto
    ): BaseResponse<String>

    @HTTP(method = "DELETE", path = "trash/images", hasBody = true)
    suspend fun fetchDeleteTrashImage(
        @Body request: TrashImageRequestDto
    ): BaseResponse<String>

    // 이미지 정보 넘기기
    @POST("images")
    suspend fun fetchSweepImages(
        @Body request: UpdateImageRequestDto
    ): BaseResponse<UpdateImageResponseDto>

    // 태그 관련 API
    @GET("tags/images/{mediaId}")
    suspend fun fetchLoadTag(
        @Path("mediaId") mediaId: Long
    ): BaseResponse<TagInfoResponseDto>

    @POST("tags/images/{imageId}")
    suspend fun fetchInputTag(
        @Path ("imageId") imageId: String,
        @Body request: TagRequestDto
    ): BaseResponse<TagResponseDto>

    @Multipart
    @POST("tags/ai")
    suspend fun fetchCreateAiTag(
        @Part image: MultipartBody.Part
    ): BaseResponse<AiTagResponseDto>

}