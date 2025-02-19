package com.umc.sweepic.presentation.record.history

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.sweepic.domain.model.request.award.ImageIdCheckRequestModel
import com.umc.sweepic.domain.model.request.award.ModifyAwardRequestModel
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel  @Inject constructor(
    private val spf: SharedPreferences,
    private val awardRepository: AwardRepository
): ViewModel(){

    private val _bestPhotos = MutableLiveData<List<String>>()
    val bestPhotos: LiveData<List<String>> get() = _bestPhotos

    fun loadSelectedBestPhotos(context: Context) {
        try {
            val file = File(context.filesDir, "selected_best_photos.json")
            if (!file.exists()) {
                Log.e("HistoryViewModel", "❌ 선택된 사진 JSON이 존재하지 않음")
                _bestPhotos.postValue(emptyList())
                return
            }

            val json = FileReader(file).use { it.readText() }
            val type = object : TypeToken<List<String>>() {}.type
            val photos: List<String> = Gson().fromJson(json, type)

            _bestPhotos.postValue(photos)
            Log.d("HistoryViewModel", "✅ 선택된 사진 불러오기 성공: $photos")
        } catch (e: Exception) {
            Log.e("HistoryViewModel", "❌ 선택된 사진 JSON 로드 실패: ${e.message}")
            _bestPhotos.postValue(emptyList())
        }
    }

    fun imageIdCheck(timestamp: String, mediaId: String){
        viewModelScope.launch {
            val request = ImageIdCheckRequestModel(timestamp = timestamp, mediaId = mediaId)

            Log.d("API_DEBUG", "요청 데이터: $request")

            awardRepository.imageIdCheck(request).onSuccess { response ->
                Log.d("Award", "imageIdCheck Api 호출 성공: $response")
            }.onFailure { error->
                Log.e("Award", "imageIdCheck Api 호출 실패: $error")
            }
        }
    }

    fun createAward(){
        viewModelScope.launch {
            awardRepository.createAward().onSuccess { response ->
                Log.d("Award", "createAward Api 호출 성공: $response")
            }.onFailure { error->
                Log.e("Award", "createAward Api 호출 실패: $error")
            }
        }
    }

    fun modifyAward(awardId: String, imageIds: List<String>) {
        viewModelScope.launch {
            val requestList = imageIds.map { ModifyAwardRequestModel(it) } // ✅ 요청 데이터 변환

            awardRepository.modifyAward(awardId, requestList)
                .onSuccess { response ->
                    Log.d("Award", "modifyAward API 호출 성공: $response")
                }
                .onFailure { error ->
                    Log.e("Award", "modifyAward API 호출 실패: ${error.message}")
                }
        }
    }

    fun getAwards(){
        viewModelScope.launch {
            awardRepository.getAwards().onSuccess { response ->
                Log.d("Award", "getAwards Api 호출 성공: $response")
            }.onFailure { error->
                Log.e("Award", "getAwards Api 호출 실패: $error")
            }
        }
    }

}