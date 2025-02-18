package com.umc.sweepic.presentation.record.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryLastBestViewModel : ViewModel() {

    private val _bestPhotos = MutableLiveData<List<String>>() // API 연동 시 사진 URL 리스트 사용
    val bestPhotos: LiveData<List<String>> get() = _bestPhotos

    fun loadBestPhotos() {
        // TODO: API에서 날짜와 사진 ID를 불러와서 UI 업데이트
    }
}
