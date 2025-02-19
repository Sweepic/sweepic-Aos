package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.MypageDataSource
import com.umc.sweepic.data.datasource.OnboardingDataSource
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel
import com.umc.sweepic.domain.repository.sweep.MypageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource,
    private val onboardingDataSource: OnboardingDataSource
) : MypageRepository {

    override suspend fun updateUserName(nameRequestDto: NameRequestDto): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = onboardingDataSource.updateUserName(nameRequestDto)

                Log.d("MypageRepositoryImpl", "이름 변경 요청: $nameRequestDto")
                Log.d("MypageRepositoryImpl", "이름 변경 응답: $response")

                if (response.resultType == "SUCCESS") {
                    Unit
                } else {
                    val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
                    throw Exception("이름 변경 실패: $errorMessage")
                }
            }.onFailure {
                Log.e("MypageRepositoryImpl", "이름 변경 오류: ${it.message}")
            }
        }

    override suspend fun updateGoalCount(goalCountRequestDto: GoalCountRequestDto): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val response = onboardingDataSource.updateGoalCount(goalCountRequestDto)

                Log.d("MypageRepositoryImpl", "목표 개수 변경 응답: $response")

                if (response.resultType == "SUCCESS") {
                    Unit
                } else {
                    val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
                    throw Exception("목표 개수 변경 실패: $errorMessage")
                }
            }.onFailure {
                Log.e("MypageRepositoryImpl", "목표 개수 변경 오류: ${it.message}")
            }
        }

    override suspend fun getUserInformation(): Result<GetUserInformationResponseModel> = withContext(Dispatchers.IO) {
        runCatching {
            val response = mypageDataSource.getUserInformation()

            if (response.resultType == "SUCCESS") {
                response.success?.toGetUserInformationResponseModel()
                    ?: throw Exception("사용자 정보가 없습니다.")
            } else {
                throw Exception("사용자 정보 요청 실패: ${response.error ?: "알 수 없는 오류"}")
            }
        }
    }

    override suspend fun withdrawal(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = mypageDataSource.withdrawal()

            if (response.resultType == "SUCCESS") {
                Unit
            } else {
                throw Exception("회원 탈퇴 실패: ${response.error ?: "알 수 없는 오류"}")
            }
        }
    }

    override suspend fun logoutUser(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val response = mypageDataSource.logoutUser()

            if (response.resultType == "SUCCESS") {
                Unit
            } else {
                throw Exception("로그아웃 실패: ${response.error ?: "알 수 없는 오류"}")
            }
        }
    }
}
