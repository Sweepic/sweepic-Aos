package com.umc.sweepic.presentation.challenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.R
import com.umc.sweepic.data.dto.response.challenge.ChallengeGetResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeDeleteResponseDto
import com.umc.sweepic.domain.model.Challenge
import com.umc.sweepic.domain.model.request.challenge.CreateChallengeUpdateRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeDeleteResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository) : ViewModel(){

    private val _updateResponse = MutableLiveData<CreateChallengeUpdateResponseModel?>()
    val updateResponse: LiveData<CreateChallengeUpdateResponseModel?> get() = _updateResponse

    private val _getChallengeResponse = MutableLiveData<List<ChallengeGetResponseModel>>()
    val getChallengeResponse: LiveData<List<ChallengeGetResponseModel>> get() = _getChallengeResponse

    private val _locationTestResponse = MutableLiveData<List<CreateLocationLogicTestResponseModel>>()
    val locationTestResponse: LiveData<List<CreateLocationLogicTestResponseModel>> get() = _locationTestResponse

    private val _locationChallengeResponse = MutableLiveData<CreateLocationChallengeResponseModel?>()
    val locationChallengeResponse: LiveData<CreateLocationChallengeResponseModel?> get() = _locationChallengeResponse

    private val _weeklyChallengeResponse = MutableLiveData<CreateWeeklyChallengeResponseModel?>()
    val weeklyChallengeResponse: LiveData<CreateWeeklyChallengeResponseModel?> get() = _weeklyChallengeResponse

    private val _newChallenges = MutableLiveData<List<Challenge>>()
    val newChallenges: LiveData<List<Challenge>> get() = _newChallenges

    private val _inProgressChallenges = MutableLiveData<List<Challenge>>()
    val inProgressChallenges: LiveData<List<Challenge>> get() = _inProgressChallenges

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        _newChallenges.value = listOf(
            Challenge("92장", "스크린캡처 앨범 정리", R.drawable.img_test),
            Challenge("100장", "몽골에서의 추억 엄선하기", R.drawable.img_test)
        )
        _inProgressChallenges.value = emptyList()
    }


    fun moveToInProgress(challenge: Challenge) {
        val currentNewChallenges = _newChallenges.value?.toMutableList() ?: mutableListOf()
        val currentInProgressChallenges = _inProgressChallenges.value?.toMutableList() ?: mutableListOf()

        if (currentNewChallenges.remove(challenge)) {
            currentInProgressChallenges.add(challenge)
        }

        _newChallenges.value = currentNewChallenges
        _inProgressChallenges.value = currentInProgressChallenges
    }

    fun fetchChallengeUpdateCreate(request: CreateChallengeUpdateRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "update API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "update API 호출 시작: $request")
                repository.fetchChallengeUpdate(request)
                    .onSuccess { response ->
                        _updateResponse.value = response
                        Log.d("ChallengeViewModel", "update API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _updateResponse.value = null
                        Log.e("ChallengeViewModel", "update API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "update API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchChallengeGet(userId: String) {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "getChallenge API 호출 시작: $userId")
                repository.fetchChallengeGet(userId)
                    .onSuccess { response ->
                        _getChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "getChallenge API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _getChallengeResponse.value = emptyList()
                        Log.e("ChallengeViewModel", "getChallenge API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "getChallenge API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchChallengeLocationLogicTestChallengeCreate(request: CreateLocationLogicTestRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "location logic test API 요청 실패: request가 null입니다.")
            _locationTestResponse.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val requestList = listOf(request)

                Log.d("ChallengeViewModel", "location logic test API 호출 시작: $requestList")
                repository.fetchChallengeLocationLogicTestChallengeCreate(requestList)
                    .onSuccess { response ->
                        _locationTestResponse.value = response
                        Log.d("ChallengeViewModel", "location logic test API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _locationTestResponse.value = emptyList()
                        Log.e("ChallengeViewModel", "location logic test API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "location logic test API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "location API 호출 시작: $request")
                repository.fetchChallengeLocationChallengeCreate(request)
                    .onSuccess { response ->
                        _locationChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "location API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _locationChallengeResponse.value = null
                        Log.e("ChallengeViewModel", "location API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "location API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "weekly API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "weekly API 호출 시작: $request")
                repository.fetchWeeklyChallengeCreate(request)
                    .onSuccess { response ->
                        _weeklyChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "weekly API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _weeklyChallengeResponse.value = null
                        Log.e("ChallengeViewModel", "weekly API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "weekly API 호출 중 예외 발생", e)
            }
        }
    }
}

