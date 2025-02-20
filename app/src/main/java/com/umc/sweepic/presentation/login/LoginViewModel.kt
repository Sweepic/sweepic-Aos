package com.umc.sweepic.presentation.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.repository.LoginRepository
import com.umc.sweepic.domain.repository.sweep.MypageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val mypageRepository: MypageRepository,
    application: Application
) : ViewModel() {

    private val _loginUrl = MutableLiveData<String>() // 카카오 로그인 URL (헷갈리지 말기!!)
    val loginUrl: LiveData<String> get() = _loginUrl

    private val _naverLoginUrl = MutableLiveData<String>() // 네이버 로그인 URL
    val naverLoginUrl: LiveData<String> get() = _naverLoginUrl

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> get() = _sessionId

    private val _userStatus = MutableLiveData<Int>() // 유저 상태( 신규회원인지, 기존 회원인지)
    val userStatus: LiveData<Int> get() = _userStatus

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val sharedPreferences =
        application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    //카카오 로그인 요청하는 함수
    fun fetchKakaoLoginUrl() {
        viewModelScope.launch {
            Log.d("LoginViewModel", "카카오 로그인 요청 시작") // 추가
            loginRepository.getKakaoLoginUrl()
                .onSuccess {
                    Log.d("LoginViewModel", "카카오 로그인 URL: $it") // 추가
                    _loginUrl.postValue(it) }
                .onFailure { _error.postValue(it.message) }
        }
    }

    fun fetchNaverLoginUrl() {
        viewModelScope.launch {
            Log.d("LoginViewModel", "네이버 로그인 요청 시작") // 추가
            loginRepository.getNaverLoginUrl()
                .onSuccess {
                    Log.d("LoginViewModel", "네이버 로그인 요청 URL: $it")
                    _loginUrl.postValue(it) }
                .onFailure {_error.postValue(it.message)}

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

    // 사용자 정보 가져와서 status 확인
    fun fetchUserStatus() {
        viewModelScope.launch {
            mypageRepository.getUserInformation()
                .onSuccess { user ->
                    Log.d("LoginViewModel", "서버에서 받은 사용자 정보: $user") // 🔍 유저 정보 전체 확인
                    _userStatus.value = user.status // 🔥 status 값 확인
                }
                .onFailure { exception ->
                    Log.e("LoginViewModel", "사용자 정보 로드 실패: ${exception.message}")
                }
        }
    }

}
