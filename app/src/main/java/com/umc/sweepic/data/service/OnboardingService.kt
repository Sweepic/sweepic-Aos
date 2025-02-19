package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.NameResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH

interface OnboardingService {
    @PATCH("onboarding/name")
    suspend fun updateUserName(@Body request: NameRequestDto) : BaseResponse<NameResponseDto>

    @PATCH("onboarding/goal")
    suspend fun updateGoalCount(@Body request: GoalCountRequestDto) : BaseResponse<Unit>
}