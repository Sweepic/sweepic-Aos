package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.OnboardingDataSource
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.toModel
import com.umc.sweepic.domain.model.response.sweep.UpdateUserNameResponseModel
import com.umc.sweepic.domain.repository.sweep.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingDataSource: OnboardingDataSource
) : OnboardingRepository {

    override suspend fun updateUserName(nameRequestDto: NameRequestDto): Result<UpdateUserNameResponseModel> {
        return runCatching {
            val response = onboardingDataSource.updateUserName(nameRequestDto)

            Log.d("OnboardingRepositoryImpl", "이름 변경 요청: $nameRequestDto")
            Log.d("OnboardingRepositoryImpl", "이름 변경 응답: $response")

            if (response.resultType == "SUCCESS") {
                response.toModel()
                    ?: throw Exception("이름 변경 응답이 비어 있습니다.") // ✅ `null`이면 예외 던짐
            } else {
                val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
                throw Exception("이름 변경 실패: $errorMessage")
            }
        }.onFailure {
            Log.e("OnboardingRepositoryImpl", "이름 변경 오류: ${it.message}")
        }
    }
    override suspend fun updateGoalCount(goalCountRequestDto: GoalCountRequestDto): Result<Unit> {
        return runCatching {
            val response = onboardingDataSource.updateGoalCount(goalCountRequestDto)

            Log.d("OnboardingRepositoryImpl", "목표 개수 변경 응답: $response")

            if (response.resultType == "SUCCESS") {
                Unit
            } else {
                val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
                throw Exception("목표 개수 변경 실패: $errorMessage")
            }
        }.onFailure {
            Log.e("OnboardingRepositoryImpl", "목표 개수 변경 오류: ${it.message}") // 오류 로그 추가
        }
    }
}
