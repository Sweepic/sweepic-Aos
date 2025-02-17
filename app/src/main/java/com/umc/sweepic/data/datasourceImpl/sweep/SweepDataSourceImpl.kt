package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.sweep.SweepDataSource
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
import com.umc.sweepic.data.service.SweepService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SweepDataSourceImpl @Inject constructor(
    private val sweepService: SweepService
): SweepDataSource {
    override suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto> =
        sweepService.fetchSweepMemoList()

    override suspend fun fetchSweepCreateTextFolder(
        folderName: RequestBody,
        base64_image: MultipartBody.Part
    ): BaseResponse<CreateTextFolderResponseDto> =
        sweepService.fetchSweepCreateTextFolder(folderName, base64_image)

    override suspend fun fetchSweepSaveTextMemo(
        folderId: Long,
        base64_image: MultipartBody.Part
    ): BaseResponse<CreateTextFolderResponseDto> =
        sweepService.fetchSweepSaveTextMemo(folderId, base64_image)

    override suspend fun fetchSweepCreateImageFolder(
        folderName: RequestBody,
        image: MultipartBody.Part,
    ): BaseResponse<CreateImageFolderResponseDto> =
        sweepService.fetchSweepCreateImageFolder(folderName, image)

    override suspend fun fetchSweepSaveImageMemo(
        folderId: Long,
        image: MultipartBody.Part,
    ): BaseResponse<SaveImageMemoResponseDto> =
        sweepService.fetchSweepSaveImageMemo(folderId, image)

    override suspend fun fetchSweepCreateMemoFolder(request: CreateMemoFolderRequestDto): BaseResponse<CreateMemoFolderResponseDto> =
        sweepService.fetchSweepCreateMemoFolder(request)

    override suspend fun fetchMoveImageToTrash(imageId: String): BaseResponse<String> =
        sweepService.fetchMoveImageToTrash(imageId)

    override suspend fun fetchRestoreTrashImage(request: TrashImageRequestDto): BaseResponse<String> =
        sweepService.fetchRestoreTrashImage(request)

    override suspend fun fetchDeleteTrashImage(request: TrashImageRequestDto): BaseResponse<String> =
        sweepService.fetchDeleteTrashImage(request)

    override suspend fun fetchSweepImages(request: UpdateImageRequestDto): BaseResponse<UpdateImageResponseDto> =
        sweepService.fetchSweepImages(request)

    override suspend fun fetchLoadTag(mediaId: Long): BaseResponse<TagInfoResponseDto> =
        sweepService.fetchLoadTag(mediaId)

    override suspend fun fetchInputTag(imageId: String, request: TagRequestDto): BaseResponse<TagResponseDto> =
        sweepService.fetchInputTag(imageId, request)

    override suspend fun fetchCreateAiTag(image: MultipartBody.Part): BaseResponse<AiTagResponseDto> =
        sweepService.fetchCreateAiTag(image)
}