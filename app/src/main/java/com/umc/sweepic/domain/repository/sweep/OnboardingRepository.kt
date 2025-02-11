package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.domain.model.response.sweep.UpdateUserNameResponseModel

interface OnboardingRepository {
    suspend fun updateUserName(nameRequestDto: NameRequestDto) : Result<UpdateUserNameResponseModel>
    suspend fun updateGoalCount (requestDto: GoalCountRequestDto) : Result<Unit>
}