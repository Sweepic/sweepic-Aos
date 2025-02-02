package com.umc.sweepic.data.repositoryImpl.sweep

import com.umc.sweepic.data.datasource.sweep.SweepDataSource
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
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
    override suspend fun fetchSweepCreateTextFolder(request: CreateTextFolderRequestModel): Result<CreateTextFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepCreateTextFolder(request.toCreateTextFolderRequestDto()).success.toCreateTextFolderResponseModel()
    }
    override suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): Result<CreateImageFolderResponseModel> = runCatching {
        sweepDataSource.fetchSweepCreateImageFolder(folderName, image).success.toCreateImageFolderResponseModel()
    }
    override suspend fun fetchSweepSaveImageMemo(folderId:Long, image: MultipartBody.Part): Result<SaveImageMemoResponseModel> = runCatching {
        sweepDataSource.fetchSweepSaveImageMemo(folderId, image).success.toSaveImageMemoResponseModel()
    }
}