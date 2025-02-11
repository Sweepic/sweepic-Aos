package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.CreateTextFolderRequestDto
import com.umc.sweepic.data.dto.request.GoalCountRequestDto
import com.umc.sweepic.data.dto.request.NameRequestDto
import com.umc.sweepic.data.dto.response.NameResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateImageFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.CreateTextFolderResponseDto
import com.umc.sweepic.data.dto.response.sweep.SaveImageMemoResponseDto
import com.umc.sweepic.data.dto.response.sweep.SweepMemoListResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface OnboardingService {
    @PATCH("onboarding/name")
    suspend fun updateUserName(@Body request: NameRequestDto) : BaseResponse<NameResponseDto>

    @PATCH("onboarding/goal")
    suspend fun updateGoalCount(@Body request: GoalCountRequestDto) : BaseResponse<Unit>
}