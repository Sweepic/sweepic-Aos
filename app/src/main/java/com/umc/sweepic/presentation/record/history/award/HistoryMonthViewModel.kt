package com.umc.sweepic.presentation.record.history.award

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import javax.inject.Inject

@HiltViewModel
class HistoryMonthViewModel @Inject constructor() : ViewModel() {

    private val _bestPhotos = MutableLiveData<List<String>>()
    val bestPhotos: LiveData<List<String>> get() = _bestPhotos

    /** 📌 1. JSON 파일에서 저번달 사진 가져오기 */
    fun loadLastMonthPhotos(context: Context) {
        val photos = getPhotosFromJson(context).take(5)
        _bestPhotos.postValue(photos)
    }

    /** 📌 2. JSON 파일에서 사진 데이터 불러오기 */
    private fun getPhotosFromJson(context: Context): List<String> {
        return try {
            val file = File(context.filesDir, "last_month_photos.json")
            if (!file.exists()) return emptyList()

            val json = FileReader(file).use { reader ->
                reader.readText()
            }

            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            Log.e("HistoryMonthViewModel", "❌ JSON 불러오기 실패: ${e.message}")
            emptyList()
        }
    }

    fun updateBestPhotos(photoPaths: List<String>) {
        Log.d("HistoryMonthViewModel", "🔥 bestPhotos 업데이트: $photoPaths") // ✅ 값이 반영되는지 확인
        _bestPhotos.value = photoPaths // ✅ 즉시 UI 업데이트
    }

    fun loadSelectedBestPhotos(context: Context) {
        val file = File(context.filesDir, "selected_best_photos.json")
        if (!file.exists()) {
            _bestPhotos.postValue(emptyList())
            return
        }

        val json = FileReader(file).use { it.readText() }
        val type = object : TypeToken<List<String>>() {}.type
        val photos: List<String> = Gson().fromJson(json, type)

        _bestPhotos.postValue(photos)
        Log.d("HistoryMonthViewModel", "✅ 선택된 사진 불러오기 성공: $photos")
    }




}
