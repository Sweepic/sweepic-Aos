package com.umc.sweepic.presentation.record.history.award

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.sweepic.data.dto.response.GetAwardResponseDto
import com.umc.sweepic.domain.model.request.award.ImageIdCheckRequestModel
import com.umc.sweepic.domain.model.request.award.ModifyAwardRequestModel
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryMonthChoiceViewModel @Inject constructor(
    private val awardRepository: AwardRepository
) : ViewModel() {

    private val _photoList = MutableLiveData<List<SelectedPhoto>>()
    val photoList: LiveData<List<SelectedPhoto>> get() = _photoList

    private val _selectedPhotos = MutableLiveData<List<SelectedPhoto>>(emptyList())
    val selectedPhotos: LiveData<List<SelectedPhoto>> get() = _selectedPhotos

    private val _bestPhotos = MutableLiveData<List<String>>()
    val bestPhotos: LiveData<List<String>> get() = _bestPhotos

    fun processAwardFlow(selectedPhotos: List<SelectedPhoto>, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                // ✅ 1. 병렬 처리로 imageIdCheck 실행
                val imageIdResults = selectedPhotos.map { photo ->
                    async {
                        awardRepository.imageIdCheck(
                            ImageIdCheckRequestModel(timestamp = photo.timestamp, mediaId = photo.mediaId)
                        ).getOrNull()?.imageId
                    }
                }.awaitAll()

                val imageIds = imageIdResults.filterNotNull()

                if (imageIds.size != selectedPhotos.size) {
                    Log.e("Award", "❌ 일부 사진의 imageId 확인 실패: $imageIdResults")
                    return@launch
                }

                val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

                // ✅ 2. 기존 어워드 조회
                val awardsResponseList = awardRepository.getAwards().getOrNull()
                var awardId = awardsResponseList?.find { it.awardMonth.substring(0, 7) == currentMonth }?.id

                // ✅ 3. 어워드 생성이 필요한 경우
                if (awardId == null) {
                    val createResponse = awardRepository.createAward().getOrNull()
                    awardId = createResponse?.id

                    if (awardId == null) {
                        Log.e("Award", "❌ 어워드 생성 실패")
                        return@launch
                    }

                    Log.d("Award", "✅ 새 어워드 생성 완료 (awardId=$awardId)")
                } else {
                    Log.d("Award", "✅ 기존 어워드 사용 (awardId=$awardId)")
                }

                // ✅ 4. modifyAward API 호출하여 새로운 사진 등록
                val requestBody = imageIds.map { ModifyAwardRequestModel(it) }
                val modifyResponse = awardRepository.modifyAward(awardId, requestBody).getOrNull()

                if (modifyResponse == null) {
                    Log.e("Award", "❌ modifyAward API 호출 실패")
                    return@launch
                }

                Log.d("Award", "✅ modifyAward API 호출 성공")
                onComplete()

            } catch (e: Exception) {
                Log.e("Award", "❌ 예외 발생: ${e.message}")
            }finally {
                onComplete()
            }
        }
    }

    fun imageIdCheck(photo: SelectedPhoto, onComplete: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = ImageIdCheckRequestModel(timestamp = photo.timestamp, mediaId = photo.mediaId)

                Log.d("API_DEBUG", "📌 요청 데이터 확인: timestamp=${photo.timestamp}, mediaId=${photo.mediaId}")

                val result = awardRepository.imageIdCheck(request)

                result.onSuccess { response ->
                    Log.d("Award", "✅ imageIdCheck API 호출 성공: $response")
                    onComplete(response.imageId) // ✅ imageId 반환
                }.onFailure { error ->
                    Log.e("Award", "❌ imageIdCheck API 호출 실패: ${error.message}")
                    onComplete(null)
                }
            } catch (e: Exception) {
                Log.e("Award", "❌ imageIdCheck 예외 발생: ${e.message}")
                onComplete(null)
            }
        }
    }

    /** 선택된 사진 추가/삭제 */
    fun togglePhotoSelection(photo: SelectedPhoto) {
        val currentList = _selectedPhotos.value?.toMutableList() ?: mutableListOf()

        if (currentList.any { it.mediaId == photo.mediaId }) {
            currentList.removeAll { it.mediaId == photo.mediaId }
            Log.d("PhotoSelection", "📌 사진 선택 해제: ${photo.photoPath}")
        } else {
            if (currentList.size < 5) {
                currentList.add(photo)
                Log.d("PhotoSelection", "✅ 사진 선택됨: ${photo.photoPath}")
            } else {
                Log.d("PhotoSelection", "⚠️ 최대 5장까지만 선택 가능합니다.")
            }
        }

        _selectedPhotos.value = currentList
        Log.d("PhotoSelection", "📷 현재 선택된 사진 리스트: $currentList")
    }


    /** 📌 1. JSON 파일에서 저번달 사진 가져오기 */
    fun loadLastMonthPhotos(context: Context) {
        val photos = getPhotosFromJson(context)
        _photoList.postValue(photos)
    }


    /** 📌 2. JSON 파일에서 사진 데이터 불러오기 */
    private fun getPhotosFromJson(context: Context): List<SelectedPhoto> {
        val file = File(context.filesDir, "last_month_photos.json")
        if (!file.exists()) return emptyList()

        val json = FileReader(file).use { reader -> reader.readText() }
        val type = object : TypeToken<List<String>>() {}.type
        val photoPaths: List<String> = Gson().fromJson(json, type) ?: emptyList()

        return photoPaths.mapNotNull { path ->
            getPhotoMetadata(context, path) // ✅ `MediaStore`를 이용해 metadata 가져오기
        }
    }

    fun getPhotoMetadata(context: Context, photoPath: String): SelectedPhoto? {
        val projection = arrayOf(
            MediaStore.Images.Media._ID, // mediaId
            MediaStore.Images.Media.DATE_TAKEN // timestamp
        )

        val selection = "${MediaStore.Images.Media.DATA} = ?"
        val selectionArgs = arrayOf(photoPath)

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

                val mediaId = cursor.getLong(idColumn).toString()
                val timestampMillis = cursor.getLong(dateColumn)
                val timestamp = formatTimestampToISO8601(timestampMillis) // ✅ ISO 8601 형식 변환 적용

                return SelectedPhoto(mediaId, timestamp, photoPath)
            }
        }
        return null
    }

    fun formatTimestampToISO8601(timestampMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC") // ✅ UTC 기준으로 변환
        return sdf.format(Date(timestampMillis))
    }

    fun updateBestPhotos(photoPaths: List<String>) {
        _bestPhotos.postValue(photoPaths) // ✅ UI 업데이트
    }



}
