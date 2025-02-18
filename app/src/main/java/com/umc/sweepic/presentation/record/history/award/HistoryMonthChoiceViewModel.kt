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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryMonthChoiceViewModel @Inject constructor() : ViewModel() {

    private val _photoList = MutableLiveData<List<String>>()
    val photoList: LiveData<List<String>> get() = _photoList

    /** 📌 1. JSON 파일에서 저번달 사진 가져오기 */
    fun loadLastMonthPhotos(context: Context) {
        val photos = getPhotosFromJson(context)
        _photoList.postValue(photos)
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
            Log.e("HistoryMonthChoiceViewModel", "❌ JSON 불러오기 실패: ${e.message}")
            emptyList()
        }
    }
}
