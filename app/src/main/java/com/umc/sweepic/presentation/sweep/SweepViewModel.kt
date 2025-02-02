package com.umc.sweepic.presentation.sweep

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.response.sweep.CreateImageFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.CreateTextFolderResponseModel
import com.umc.sweepic.domain.model.response.sweep.SaveImageMemoResponseModel
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
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

        // 🔥 휴지통에 있는 이미지들 가져오기
        val trashedUris = TrashRepository.getAllTrashed().map { it.uri }

        // 🔥 휴지통에 없는 이미지들만 필터링
        _imagesLiveData.value = allImages.filterNot { trashedUris.contains(it.uri) }
    }

    fun refreshImages() {
        val allImages = galleryRepository.getAllGalleryImagesDesc()

        // 🔥 휴지통 이미지를 제외
        val trashedUris = TrashRepository.getAllTrashed().map { it.uri }
        _imagesLiveData.value = allImages.filterNot { trashedUris.contains(it.uri) }
    }

    suspend fun fetchSweepCreateTextFolder(request: CreateTextFolderRequestModel): Result<CreateTextFolderResponseModel> {
        return repository.fetchSweepCreateTextFolder(request)
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
}