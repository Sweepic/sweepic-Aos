package com.umc.sweepic.presentation.record.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel
import com.umc.sweepic.domain.repository.sweep.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryTagViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _mostTaggedData = MutableLiveData<GetMostTaggedModel>()
    val mostTaggedData: LiveData<GetMostTaggedModel> get() = _mostTaggedData

    fun fetchMostTaggedData() {
        viewModelScope.launch {
            historyRepository.getMostTagged()
                .onSuccess { data ->
                    Log.d("HistoryViewModel", "Most Tagged Data: $data")
                    _mostTaggedData.postValue(data)
                }
                .onFailure { error ->
                    Log.e("HistoryViewModel", "Error fetching data: $error")
                    _mostTaggedData.postValue(GetMostTaggedModel(emptyList()))
                }
        }
    }

//    private val _tagsByMonth = MutableLiveData<Map<Double, List<GetTagsByDateModel.TagsByDateItem>>>()
//    val tagsByMonth: LiveData<Map<Double, List<GetTagsByDateModel.TagsByDateItem>>> get() = _tagsByMonth
//
//    private var currentYear = 2024.0
//
//    fun fetchTagsByYear(year: Double) {
//        viewModelScope.launch {
//            val monthTagsMap = mutableMapOf<Double, List<GetTagsByDateModel.TagsByDateItem>>()
//
//            for (month in 1..12) { // 1월~12월 데이터 가져오기
//                historyRepository.getTagsByDate(year, month.toDouble(), null)
//                    .onSuccess { data ->
//                        monthTagsMap[month.toDouble()] = data.tags
//                    }
//                    .onFailure {
//                        monthTagsMap[month.toDouble()] = emptyList()
//                    }
//            }
//            _tagsByMonth.postValue(monthTagsMap)
//        }
//    }
//
//    fun setYear(year: Double) {
//        currentYear = year
//        fetchTagsByYear(year)
//    }
}