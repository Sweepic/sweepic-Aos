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
            val tagsByMonth = mutableMapOf<Double, List<GetMostTaggedModel.MostTaggedItem>>()

            val startMonth = month ?: if (year == currentYear) currentMonth else 12.0

            for (m in startMonth.toInt() downTo 1) {
                historyRepository.getMostTagged(year, m.toDouble())
                    .onSuccess { data ->
                        tagsByMonth[m.toDouble()] = data.success
                    }
                    .onFailure {
                        tagsByMonth[m.toDouble()] = emptyList()
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
