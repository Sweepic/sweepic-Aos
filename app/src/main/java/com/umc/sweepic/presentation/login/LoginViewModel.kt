package com.umc.sweepic.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginUrl = MutableLiveData<String>()
    val loginUrl: LiveData<String> get() = _loginUrl

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> get() = _sessionId

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchKakaoLoginUrl() {
        viewModelScope.launch {
            loginRepository.getKakaoLoginUrl()
                .onSuccess { _loginUrl.postValue(it) }
                .onFailure { _error.postValue(it.message) }
        }
    }

    fun requestSessionId() {
        viewModelScope.launch {
            loginRepository.kakaoLoginCallback()
                .onSuccess { response ->
                    val sessionId = response.token
                    if (sessionId.isNotEmpty()) {
                        _sessionId.postValue(sessionId)
                    } else {
                        _error.postValue("세션 ID 없음")
                    }
                }
                .onFailure { _error.postValue(it.message) }
        }
    }
}
