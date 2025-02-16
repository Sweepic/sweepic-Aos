package com.umc.sweepic.presentation.sweep

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.request.sweep.MoveTrashRequestModel
import com.umc.sweepic.domain.model.request.sweep.TagRequestModel
import com.umc.sweepic.domain.model.request.sweep.UpdateImageRequestModel
import com.umc.sweepic.domain.model.response.sweep.AiTagResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.MoveTrashResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagResponseModel
import com.umc.sweepic.domain.model.response.sweep.UpdateImageResponseModel
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import com.umc.sweepic.domain.repository.sweep.TrashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class SweepViewModel @Inject constructor(
    private val repository: SweepRepository,
    private val galleryRepository: GalleryRepository
): ViewModel() {
    private val _folderList = MutableLiveData<List<SweepMemoListModel.MemoFolderModel>>()
    val folderList: LiveData<List<SweepMemoListModel.MemoFolderModel>> = _folderList

    private val _imagesLiveData = MutableLiveData<List<Gallery>>()
    val imagesLiveData: LiveData<List<Gallery>> = _imagesLiveData

    private val _updateImageResult = MutableLiveData<UpdateImageResponseModel>()
    val updateImageResult: LiveData<UpdateImageResponseModel> = _updateImageResult

    private val _tagResponse = MutableLiveData<TagResponseModel>()
    val tagResponse: LiveData<TagResponseModel> = _tagResponse

    private val _tagInfoResponse = MutableLiveData<BaseResponse<TagInfoResponseModel>?>()
    val tagInfoResponse: LiveData<BaseResponse<TagInfoResponseModel>?> get() = _tagInfoResponse

    private val _aiTagResponse = MutableLiveData<BaseResponse<AiTagResponseModel>?>()
    val aiTagResponse: LiveData<BaseResponse<AiTagResponseModel>?> get() = _aiTagResponse


    fun fetchFolderList() {
        viewModelScope.launch {
            repository.fetchSweepMemoList().onSuccess { memoList ->
                _folderList.value = memoList.data
                Log.d("MemoList", memoList.toString())
            }.onFailure { exception ->
                Log.e("MemoList", exception.stackTraceToString())
            }
        }
    }

    // 갤러리 이미지 로드
    fun loadImages() {
        val allImages = galleryRepository.getAllGalleryImagesDesc()

        // 휴지통에 있는 이미지들 가져오기
        val trashedUris = TrashRepository.getAllTrashed().map { it.uri }

        // 휴지통에 없는 이미지들만 필터링
        _imagesLiveData.value = allImages.filterNot { trashedUris.contains(it.uri) }
    }

    fun refreshImages() {
        val allImages = galleryRepository.getAllGalleryImagesDesc()

        // 휴지통 이미지를 제외
        val trashedUris = TrashRepository.getAllTrashed().map { it.uri }
        _imagesLiveData.value = allImages.filterNot { trashedUris.contains(it.uri) }
    }

    suspend fun fetchSweepCreateTextFolder(folderName: String, image: ByteArray): Result<CreateTextFolderResponseModel> {
        return try {
            val folderRequestBody = folderName.toRequestBody("text/plain".toMediaType()) // 폴더 이름을 RequestBody로 변환
            val imageRequestBody = image.toRequestBody("image/jpeg".toMediaTypeOrNull()) // 이미지 바이트 배열을 RequestBody로 변환
            val imagePart = MultipartBody.Part.createFormData("base64_image", "image.jpg", imageRequestBody)

            repository.fetchSweepCreateTextFolder(folderRequestBody, imagePart) // API 호출
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchSweepSaveTextMemo(folderId: Long, image: MultipartBody.Part): Result<CreateTextFolderResponseModel> {
        return repository.fetchSweepSaveTextMemo(folderId, image)
    }

    suspend fun fetchSweepCreateImageFolder(folderName: String, image: ByteArray): Result<CreateImageFolderResponseModel> {
        return try {
            val folderRequestBody = folderName.toRequestBody("text/plain".toMediaType()) // 폴더 이름을 RequestBody로 변환
            val imageRequestBody = image.toRequestBody("image/jpeg".toMediaTypeOrNull()) // 이미지 바이트 배열을 RequestBody로 변환
            val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody)

            repository.fetchSweepCreateImageFolder(folderRequestBody, imagePart) // API 호출
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchSweepSaveImageMemo(folderId: Long, image: MultipartBody.Part): Result<SaveImageMemoResponseModel> {
        return repository.fetchSweepSaveImageMemo(folderId, image)
    }

    suspend fun fetchSweepMoveToTrash(request: MoveTrashRequestModel): Result<MoveTrashResponseModel> {
        return repository.fetchSweepMoveToTrash(request)
    }

    //
    fun fetchSweepImages(request: UpdateImageRequestModel) {
        viewModelScope.launch {
            repository.fetchSweepImages(request)
                .onSuccess { response ->
                    _updateImageResult.value = response
                    Log.d("fetchSweepImages", "이미지 업데이트 성공: ${response.imageId}")
                }
                .onFailure { exception ->
                    Log.e("fetchSweepImages", "이미지 업데이트 실패: ${exception.message}")
                }
        }
    }

    // 태그 입력
    fun fetchInputTag(imageId: String, request: TagRequestModel) {
        viewModelScope.launch {
            repository.fetchInputTag(imageId, request)
                .onSuccess { response ->
                    _tagResponse.value = response
                    Log.d("fetchInputTag", "Tag update success: ${response.tags}")
                }
                .onFailure { exception ->
                    Log.e("fetchInputTag", "Tag update failed: ${exception.message}")
                }
        }
    }

    // 태그 정보 API
    fun loadTagForMedia(mediaId: Long) {
        viewModelScope.launch {
            repository.fetchLoadTag(mediaId)
                .onSuccess { response ->
                    _tagInfoResponse.value = BaseResponse(resultType = "SUCCESS", error = null, success = response)
                    Log.d("loadTagForMedia", "태그 로드 성공: ${response.tags}")
                }
                .onFailure { exception ->
                    Log.e("loadTagForMedia", "태그 로드 실패: ${exception.message}")
                    _tagInfoResponse.value = null
                }
        }
    }

    fun fetchCreateAiTag(imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            repository.fetchCreateAiTag(imagePart)
                .onSuccess { response ->
                    _aiTagResponse.value = BaseResponse(resultType = "SUCCESS", error = null, success = response)
                    Log.d("SweepViewModel", "AI Tag fetch success: ${response.labels}")
                }
                .onFailure { exception ->
                    Log.e("SweepViewModel", "AI Tag fetch failed: ${exception.message}")
                    _aiTagResponse.value = null
                }
        }
    }
}