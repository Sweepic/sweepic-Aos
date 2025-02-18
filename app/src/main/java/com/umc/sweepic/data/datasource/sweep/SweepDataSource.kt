package com.umc.sweepic.data.datasource.sweep

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.sweep.CreateMemoFolderRequestDto
import com.umc.sweepic.data.dto.request.sweep.TagRequestDto
import com.umc.sweepic.data.dto.request.sweep.TrashImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import com.umc.sweepic.data.dto.response.sweep.AiTagResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateMemoFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagResponseDto
import com.umc.sweepic.data.dto.response.sweep.UpdateImageResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SweepDataSource {
    suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto>
    suspend fun fetchSweepCreateTextFolder(folderName: RequestBody, image: MultipartBody.Part): BaseResponse<CreateTextFolderResponseDto>
    suspend fun fetchSweepSaveTextMemo(folderId: Long, image: MultipartBody.Part): BaseResponse<CreateTextFolderResponseDto>
    suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): BaseResponse<CreateImageFolderResponseDto>
    suspend fun fetchSweepSaveImageMemo(folderId: Long, image: MultipartBody.Part): BaseResponse<SaveImageMemoResponseDto>
    suspend fun fetchSweepCreateMemoFolder(request: CreateMemoFolderRequestDto): BaseResponse<CreateMemoFolderResponseDto>
    suspend fun fetchMoveImageToTrash(imageId: String): BaseResponse<String>
    suspend fun fetchRestoreTrashImage(request: TrashImageRequestDto): BaseResponse<String>
    suspend fun fetchDeleteTrashImage(request: TrashImageRequestDto): BaseResponse<String>
    suspend fun fetchSweepImages(request: UpdateImageRequestDto): BaseResponse<UpdateImageResponseDto>
    suspend fun fetchLoadTag(mediaId: Long): BaseResponse<TagInfoResponseDto>
    suspend fun fetchInputTag(imageId: String, request: TagRequestDto): BaseResponse<TagResponseDto>
    suspend fun fetchCreateAiTag(image: MultipartBody.Part): BaseResponse<AiTagResponseDto>
}