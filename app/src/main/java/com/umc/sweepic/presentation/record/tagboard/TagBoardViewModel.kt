package com.umc.sweepic.presentation.record.tagboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.response.sweep.DateTagsResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel
import com.umc.sweepic.domain.repository.sweep.TagboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagBoardViewModel @Inject constructor(
    private val tagboardRepository: TagboardRepository
) : ViewModel() {

    private val _imageTags = MutableLiveData<TagInfoResponseModel?>()
    val imageTags: LiveData<TagInfoResponseModel?> get() = _imageTags

    private val _imageDateTags = MutableLiveData<DateTagsResponseModel?>()
    val imageDateTags: LiveData<DateTagsResponseModel?> get() = _imageDateTags


    fun fetchImageTags(mediaId: Double) {
        viewModelScope.launch {
            tagboardRepository.getImageTags(mediaId)
                .onSuccess { response ->
                    _imageTags.postValue(response)
                    Log.d("tagboard", "이미지 태그 조회 성공: $response")
                }
                .onFailure { error ->
                    Log.e("tagboard", "이미지 태그 조회 실패: ${error.message}")
                }
        }
    }

    fun fetchDateTags(year: Double, month: Double, date: Double, callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            tagboardRepository.getDateTags(year, month, date)
                .onSuccess { response ->
                    _imageDateTags.postValue(response)
                    Log.d("tagboard", "날짜 기반 태그 조회 성공: $response")

                    val tags = response.tags
                    if (tags.isNotEmpty()) {
                        Log.d("tagboard", "날짜: $year-$month-$date, 태그: $tags")
                    } else {
                        Log.d("tagboard", "날짜: $year-$month-$date, 태그가 없음")
                    }
                    callback(tags) // 태그 리스트 반환
                }
                .onFailure { error ->
                    Log.e("tagboard", "날짜 기반 태그 조회 실패: ${error.message}")
                    callback(emptyList()) // 실패 시 빈 리스트 반환
                }
        }
    }


}
