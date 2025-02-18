package com.umc.sweepic.presentation.record.history

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.request.award.ModifyAwardRequestModel
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel  @Inject constructor(
    private val spf: SharedPreferences,
    private val awardRepository: AwardRepository
): ViewModel(){
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