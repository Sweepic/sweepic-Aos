package com.umc.sweepic.presentation.login

import android.app.Application
import android.content.Context
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
    private val loginRepository: LoginRepository,
    application: Application
) : ViewModel() {

    private val _loginUrl = MutableLiveData<String>()
    val loginUrl: LiveData<String> get() = _loginUrl

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> get() = _sessionId

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    private val sharedPreferences =
        application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    //카카오 로그인 요청하는 함수
    fun fetchKakaoLoginUrl() {
        viewModelScope.launch {
            loginRepository.getKakaoLoginUrl()
                .onSuccess { _loginUrl.postValue(it) }
                .onFailure { _error.postValue(it.message) }
        }
    }

    //세션 아이디를 저장하는 함수
    fun setSessionId(sessionId: String) {
        _sessionId.postValue(sessionId)
        with(sharedPreferences.edit()) {
            putString("SESSION_ID", sessionId)
            apply()
        }
    }
}
