package com.umc.sweepic.presentation.record.history.award

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import com.umc.sweepic.domain.model.Award
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryLastBestViewModel @Inject constructor(
    private val awardRepository: AwardRepository,
    private val context: Context // 🔹 기기 내 사진 접근을 위해 Context 추가
) : ViewModel() {

    private val _awards = MutableLiveData<List<Award>>()
    val awards: LiveData<List<Award>> get() = _awards

    fun getAwards() {
        viewModelScope.launch {
            awardRepository.getAwards().onSuccess { response ->
                Log.d("Award", "getAwards API 호출 성공: $response")

                val awardList = response.map { award ->
                    val mediaIdList = listOf(award.id) // `id`가 `mediaId`임
                    val photoUris = getPhotosFromMediaIds(context, mediaIdList)

                    Log.d("Award", "mediaId: ${award.id}, photoUris: $photoUris") // ✅ 로그 추가

                    Award(
                        id = award.id.toIntOrNull() ?: 0,
                        date = formatAwardMonth(award.awardMonth),
                        photoUris = photoUris
                    )
                }

                Log.d("Award", "최종 Award 리스트: $awardList") // ✅ 전체 리스트 로그 출력
                _awards.postValue(awardList)
            }.onFailure { error ->
                Log.e("Award", "getAwards API 호출 실패: $error")
            }
        }
    }


    // 🔹 `mediaId`를 이용하여 기기 내 사진의 URI 가져오기
    private fun getPhotosFromMediaIds(context: Context, mediaIds: List<String>): List<Uri> {
        val photoUris = mutableListOf<Uri>()
        val contentResolver: ContentResolver = context.contentResolver

        for (mediaId in mediaIds) {
            val uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediaId)
            photoUris.add(uri)
        }

        return if (photoUris.isNotEmpty()) photoUris else listOf(Uri.EMPTY) // 기본값 설정
    }

    // 날짜 변환: "2025년 02월" 형식으로 변환
    private fun formatAwardMonth(dateString: String): String {
        return dateString.substring(0, 7).replace("-", "년 ") + "월"
    }
}
