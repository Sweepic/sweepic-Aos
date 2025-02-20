package com.umc.sweepic.presentation.mypage

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel
import com.umc.sweepic.domain.repository.sweep.MypageRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    application: Application,
    private val repository: MypageRepository
) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    private val _nameUpdateStatus = MutableLiveData<Boolean>()
    val nameUpdateStatus: LiveData<Boolean> = _nameUpdateStatus

    private val _updatedName = MutableLiveData<String>()
    val updatedName: LiveData<String> = _updatedName

    private val _goalUpdateStatus = MutableLiveData<Boolean>()
    val goalUpdateStatus: LiveData<Boolean> = _goalUpdateStatus

    private val _updatedGoalCount = MutableLiveData<Int>()
    val updatedGoalCount: LiveData<Int> = _updatedGoalCount


    private val _userInfo = MutableLiveData<GetUserInformationResponseModel?>()
    val userInfo: LiveData<GetUserInformationResponseModel?> = _userInfo

    private val _logoutStatus = MutableLiveData<Boolean>()
    val logoutStatus: LiveData<Boolean> = _logoutStatus

    private val _withdrawalStatus = MutableLiveData<Boolean>()
    val withdrawalStatus: LiveData<Boolean> = _withdrawalStatus

    //이름 수정
    fun updateUserName(newName: String) {
        viewModelScope.launch {
            repository.updateUserName(NameRequestDto(newName))
                .onSuccess {
                    _updatedName.value = newName
                    _nameUpdateStatus.value = true
                    Log.d("MypageViewModel", "이름 변경 성공")
                }
                .onFailure { exception ->
                    _nameUpdateStatus.value = false
                    Log.e("MypageViewModel", "이름 변경 실패: ${exception.message}")
                }
        }
    }

    //목표 수정
    fun updateGoalCount(newGoalCount: Int) {
        viewModelScope.launch {
            repository.updateGoalCount(GoalCountRequestDto(newGoalCount))
                .onSuccess {
                    _updatedGoalCount.value = newGoalCount
                    _goalUpdateStatus.value = true
                    Log.d("MypageViewModel", "목표 변경 성공")
                }
                .onFailure { exception ->
                    _goalUpdateStatus.value = false
                    Log.e("MypageViewModel", "목표 변경 실패: ${exception.message}")
                }
        }
    }

    // 사용자 정보 가져오기
    fun fetchUserInfo() {
        viewModelScope.launch {
            repository.getUserInformation()
                .onSuccess { user ->
                    _userInfo.value = user
                    Log.d("MypageViewModel", "사용자 정보 로드 성공: $user")
                }
                .onFailure { exception ->
                    Log.e("MypageViewModel", "사용자 정보 로드 실패: ${exception.message}")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val sharedPreferences = getApplication<Application>().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val sessionId = sharedPreferences.getString("SESSION_ID", null)

                // 현재 저장된 세션 ID 로그로 출력해서 확인!!
                Log.d("MypageViewModel", "현재 세션 ID: $sessionId")

                if (sessionId.isNullOrEmpty()) {
                    Log.e("MypageViewModel", "로그아웃 실패: 세션 ID 없음")
                    _logoutStatus.value = false
                    return@launch
                }

                repository.logoutUser()
                    .onSuccess {
                        _logoutStatus.value = true
                        Log.d("MypageViewModel", "로그아웃 성공")
                        clearSession()
                    }
                    .onFailure { exception ->
                        _logoutStatus.value = false
                        Log.e("MypageViewModel", "로그아웃 실패: ${exception.message}")
                    }
            } catch (e: Exception) {
                _logoutStatus.value = false
                Log.e("MypageViewModel", "로그아웃 요청 실패", e)
            }
        }
    }
    private fun clearSession() {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("SESSION_ID")
            apply()
        }
        Log.d("MypageViewModel", "세션 정보 삭제 완료")
    }

    // 회원 탈퇴 요청
    fun withdrawal() {
        viewModelScope.launch {
            repository.withdrawal()
                .onSuccess {
                    _withdrawalStatus.value = true
                    Log.d("MypageViewModel", "회원 탈퇴 성공")
                }
                .onFailure { exception ->
                    _withdrawalStatus.value = false
                    Log.e("MypageViewModel", "회원 탈퇴 실패: ${exception.message}")
                }
        }
    }
}
