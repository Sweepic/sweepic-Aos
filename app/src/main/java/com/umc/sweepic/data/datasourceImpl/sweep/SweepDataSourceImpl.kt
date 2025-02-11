package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.sweep.SweepDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.CreateTextFolderRequestDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
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

}