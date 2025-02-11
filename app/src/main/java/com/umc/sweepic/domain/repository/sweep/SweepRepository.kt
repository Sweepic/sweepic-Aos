package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SweepRepository {
    suspend fun fetchSweepMemoList(): Result<SweepMemoListModel>
    suspend fun fetchSweepCreateTextFolder(folder_name: RequestBody, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel>
    suspend fun fetchSweepSaveTextMemo(folderId: Number, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel>
    suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): Result<CreateImageFolderResponseModel>
    suspend fun fetchSweepSaveImageMemo(folderId:Long, image: MultipartBody.Part): Result<SaveImageMemoResponseModel>
}