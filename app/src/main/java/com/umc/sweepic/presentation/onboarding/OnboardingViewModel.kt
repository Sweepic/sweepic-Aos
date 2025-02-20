package com.umc.sweepic.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.domain.repository.sweep.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    //이름 변경 성공 여부
    private val _nameUpdateResult = MutableLiveData<Result<String>>()
    val nameUpdateResult: LiveData<Result<String>> get() = _nameUpdateResult

    //목표 장수 성공 여부
    private val _goalUpdateResult = MutableLiveData<Result<Unit>>()
    val goalUpdateResult: LiveData<Result<Unit>> get() = _goalUpdateResult

    // 이름 변경 API 호출 함수
    fun updateUserName(name: String) {
        viewModelScope.launch {
            val request = NameRequestDto(name = name)
            val result = onboardingRepository.updateUserName(request)
            _nameUpdateResult.value = result.map { it.name }
        }
    }
    fun updateGoalCount(goalCount: Int) {
        viewModelScope.launch {
            val request = GoalCountRequestDto(goalCount)
            val result = onboardingRepository.updateGoalCount(request)
            _goalUpdateResult.value = result
        }
    }
}
