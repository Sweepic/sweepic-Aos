package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChallengeService {
    @POST("challenge/location_logic/test")
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(
        @Body request: CreateLocationLogicTestRequestDto
    ): BaseResponse<CreateLocationLogicTestResponseDto>

    /*@POST("challenge/location_challenge/create")
    suspend fun fetchSweepLocationChallengeCreate(
        @Body request: CreateLocationChallengeRequestDto
    ): BaseResponse<CreateLocationChallengeResponseDto>

    @POST("challenge/weekly_challenge/create")
    suspend fun fetchSweepWeeklyChallengeCreate(
        @Body request: CreateWeeklyChallengeRequestDto
    ): BaseResponse<CreateWeeklyChallengeResponseDto>*/
}