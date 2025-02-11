package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.OnboardingDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.NameResponseDto
import com.umc.sweepic.data.service.OnboardingService
import javax.inject.Inject

class OnboardingDataSourceImpl @Inject constructor (
    private val onboardingService : OnboardingService ) : OnboardingDataSource {
    override suspend fun updateUserName(requestDto: NameRequestDto): BaseResponse<NameResponseDto>
            = onboardingService.updateUserName(requestDto)

    override suspend fun updateGoalCount(requestDto: GoalCountRequestDto): BaseResponse<Unit>
        = onboardingService.updateGoalCount(requestDto)
}
