package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.NameResponseDto

interface OnboardingDataSource {
    suspend fun updateUserName(requestDto: NameRequestDto): BaseResponse<NameResponseDto>
    suspend fun updateGoalCount (request: GoalCountRequestDto) : BaseResponse<Unit>
}