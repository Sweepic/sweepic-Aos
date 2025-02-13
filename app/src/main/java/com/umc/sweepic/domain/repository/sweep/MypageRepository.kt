package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.mypage.GetUserInformationResponseDto
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel

interface MypageRepository {
    suspend fun getUserInformation(): Result<GetUserInformationResponseModel>
    suspend fun withdrawal(): Result<Unit>
    suspend fun logoutUser(): Result<Unit>

    suspend fun updateUserName(request: NameRequestDto): Result<Unit>
    suspend fun updateGoalCount(request: GoalCountRequestDto): Result<Unit>
}