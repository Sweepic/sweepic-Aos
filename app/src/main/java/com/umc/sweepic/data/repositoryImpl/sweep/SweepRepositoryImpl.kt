package com.umc.sweepic.data.repositoryImpl.sweep

import com.umc.sweepic.data.datasource.sweep.SweepDataSource
import com.umc.sweepic.data.dto.request.sweep.CreateMemoFolderRequestDto
import com.umc.sweepic.data.dto.request.sweep.DeleteImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.MoveTrashRequestDto
import com.umc.sweepic.data.dto.request.sweep.TrashImageRequestDto
import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import com.umc.sweepic.data.dto.response.sweep.CreateMemoFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.DeleteImageResponseDto
import com.umc.sweepic.data.dto.response.sweep.MoveTrashResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.UpdateImageResponseDto
import com.umc.sweepic.domain.model.request.sweep.CreateMemoFolderRequestModel
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.request.sweep.DeleteImageRequestModel
import com.umc.sweepic.domain.model.request.sweep.MoveTrashRequestModel
import com.umc.sweepic.domain.model.request.sweep.TagRequestModel
import com.umc.sweepic.domain.model.request.sweep.TrashImageRequestModel
import com.umc.sweepic.domain.model.request.sweep.UpdateImageRequestModel
import com.umc.sweepic.domain.model.response.sweep.AiTagResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateMemoFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.DeleteImageResponseModel
import com.umc.sweepic.domain.model.response.sweep.MoveTrashResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagResponseModel
import com.umc.sweepic.domain.model.response.sweep.UpdateImageResponseModel
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SweepRepositoryImpl @Inject constructor(
    private val sweepDataSource: SweepDataSource
): SweepRepository {
    override suspend fun fetchSweepMemoList(): Result<SweepMemoListModel> = runCatching {
        sweepDataSource.fetchSweepMemoList().success.toSweepMemoListModel()
    }
    override suspend fun fetchSweepCreateTextFolder(folderName: RequestBody, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepCreateTextFolder(folderName, base64_image).success.toCreateTextFolderResponseModel()
    }
    override suspend fun fetchSweepSaveTextMemo(folderId: Long, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepSaveTextMemo(folderId, base64_image).success.toCreateTextFolderResponseModel()
    }
    override suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): Result<CreateImageFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepCreateImageFolder(folderName, image).success.toCreateImageFolderResponseModel()
    }
    override suspend fun fetchSweepSaveImageMemo(folderId:Long, image: MultipartBody.Part): Result<SaveImageMemoResponseModel> = runCatching {
        sweepDataSource.fetchSweepSaveImageMemo(folderId, image).success.toSaveImageMemoResponseModel()
    }
    override suspend fun fetchSweepCreateMemoFolder(request: CreateMemoFolderRequestModel): Result<CreateMemoFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepCreateMemoFolder(request.toCreateMemoFolderRequestDto()).success.toCreateMemoFolderResponseModel()
    }
    override suspend fun fetchMoveImageToTrash(imageId: String): Result<String> = runCatching {
        sweepDataSource.fetchMoveImageToTrash(imageId).success
    }
    override suspend fun fetchRestoreTrashImage(request: TrashImageRequestModel): Result<String> = runCatching {
        sweepDataSource.fetchRestoreTrashImage(request.toTrashImageRequestDto()).success
    }
    override suspend fun fetchDeleteTrashImage(request: TrashImageRequestModel): Result<String> = runCatching {
        sweepDataSource.fetchDeleteTrashImage(request.toTrashImageRequestDto()).success
    }

    override suspend fun fetchSweepImages(request: UpdateImageRequestModel): Result<UpdateImageResponseModel> = runCatching {
        sweepDataSource.fetchSweepImages(request.toUpdateImageRequestDto()).success.toUpdateImageResponseModel()
    }

    override suspend fun fetchLoadTag(mediaId: Long): Result<TagInfoResponseModel> = runCatching {
        sweepDataSource.fetchLoadTag(mediaId).success.toTagInfoResponseModel()
    }

    override suspend fun fetchInputTag(imageId: String, request: TagRequestModel): Result<TagResponseModel> = runCatching {
        sweepDataSource.fetchInputTag(imageId, request.toTagRequestDto()).success.toTagRequestModel()
    }

    override suspend fun fetchCreateAiTag(image: MultipartBody.Part): Result<AiTagResponseModel> = runCatching {
        sweepDataSource.fetchCreateAiTag(image).success.toAiTagResponseModel()
    }
}