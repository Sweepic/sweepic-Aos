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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryTagViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _mostTaggedData = MutableLiveData<Map<Double, List<GetMostTaggedModel.MostTaggedItem>>>()
    val mostTaggedData: LiveData<Map<Double, List<GetMostTaggedModel.MostTaggedItem>>> get() = _mostTaggedData

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR).toDouble()
    private val currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1).toDouble()

    private var selectedYear: Double = currentYear


    fun fetchMostTaggedData(year: Double, month: Double? = null) {
        viewModelScope.launch {
            Log.d("HistoryTagViewModel", "Fetching most tagged data for Year: $year, Month: ${month ?: "전체"}")

            val tagsByMonth = mutableMapOf<Double, List<GetMostTaggedModel.MostTaggedItem>>()
            val startMonth = month ?: if (year == currentYear) currentMonth else 12.0

            for (m in startMonth.toInt() downTo 1) {
                Log.d("HistoryTagViewModel", "Calling repository for Year: $year, Month: $m")

                historyRepository.getMostTagged(year, m.toDouble())
                    .onSuccess { data ->
                        Log.d("HistoryTagViewModel", "Success: Received ${data.success.size} tags for Month: $m")
                        if (data.success.isNotEmpty()) {
                            tagsByMonth[m.toDouble()] = data.success
                        }
                    }
                    .onFailure { error ->
                        Log.e("HistoryTagViewModel", "Error fetching most tagged data for Month: $m - ${error.message}")
                    }
            }
            _mostTaggedData.postValue(tagsByMonth)
        }
    }


    fun setYear(year: Double) {
        if (year > currentYear) return
        selectedYear = year
        fetchMostTaggedData(selectedYear)
    }
}
