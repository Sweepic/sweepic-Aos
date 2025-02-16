package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.data.dto.request.sweep.UpdateImageRequestDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto
import com.umc.sweepic.data.dto.response.sweep.UpdateImageResponseDto
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.request.sweep.DeleteImageRequestModel
import com.umc.sweepic.domain.model.request.sweep.MoveTrashRequestModel
import com.umc.sweepic.domain.model.request.sweep.TagRequestModel
import com.umc.sweepic.domain.model.request.sweep.UpdateImageRequestModel
import com.umc.sweepic.domain.model.response.sweep.AiTagResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.DeleteImageResponseModel
import com.umc.sweepic.domain.model.response.sweep.MoveTrashResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagResponseModel
import com.umc.sweepic.domain.model.response.sweep.UpdateImageResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SweepRepository {
    suspend fun fetchSweepMemoList(): Result<SweepMemoListModel>
    suspend fun fetchSweepCreateTextFolder(folderName: RequestBody, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel>
    suspend fun fetchSweepSaveTextMemo(folderId: Long, base64_image: MultipartBody.Part): Result<CreateTextFolderResponseModel>
    suspend fun fetchSweepCreateImageFolder(folderName: RequestBody, image: MultipartBody.Part): Result<CreateImageFolderResponseModel>
    suspend fun fetchSweepSaveImageMemo(folderId:Long, image: MultipartBody.Part): Result<SaveImageMemoResponseModel>
    suspend fun fetchSweepDeleteImage(request: DeleteImageRequestModel): Result<DeleteImageResponseModel>
    suspend fun fetchSweepMoveToTrash(request: MoveTrashRequestModel): Result<MoveTrashResponseModel>
    suspend fun fetchSweepImages(request: UpdateImageRequestModel): Result<UpdateImageResponseModel>
    suspend fun fetchLoadTag(mediaId:Long): Result<TagInfoResponseModel>
    suspend fun fetchInputTag(imageId: String, request: TagRequestModel): Result<TagResponseModel>
    suspend fun fetchCreateAiTag(image: MultipartBody.Part): Result<AiTagResponseModel>
}