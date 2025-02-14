package com.umc.sweepic.data.datasource.sweep

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

interface SweepDataSource {
    suspend fun fetchSweepMemoList(): BaseResponse<SweepMemoListResponseDto>
    suspend fun fetchSweepCreateTextFolder(folderName: RequestBody, base64_image: MultipartBody.Part): BaseResponse<CreateTextFolderResponseDto>
    suspend fun fetchSweepSaveTextMemo(folderId: Long, base64_image: MultipartBody.Part): BaseResponse<CreateTextFolderResponseDto>
    suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): BaseResponse<CreateImageFolderResponseDto>
    suspend fun fetchSweepSaveImageMemo(folderId: Long, image: MultipartBody.Part): BaseResponse<SaveImageMemoResponseDto>
    suspend fun fetchSweepDeleteImage(request: DeleteImageRequestDto): BaseResponse<DeleteImageResponseDto>
    suspend fun fetchSweepMoveToTrash(request: MoveTrashRequestDto): BaseResponse<MoveTrashResponseDto>
    suspend fun fetchSweepImages(request: UpdateImageRequestDto): BaseResponse<UpdateImageResponseDto>
}