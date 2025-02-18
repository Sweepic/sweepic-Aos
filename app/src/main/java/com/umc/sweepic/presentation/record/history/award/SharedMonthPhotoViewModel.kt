package com.umc.sweepic.presentation.record.history.award

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedMonthPhotoViewModel @Inject constructor() : ViewModel() {

    // 📌 LiveData로 사진 리스트 관리
    private val _photoList = MutableLiveData<List<String>>()
    val photoList: LiveData<List<String>> get() = _photoList

    // 📌 사진 리스트 저장
    fun setPhotoList(photos: List<String>) {
        _photoList.postValue(photos) // 메인 스레드에서 UI 업데이트
    }
}
