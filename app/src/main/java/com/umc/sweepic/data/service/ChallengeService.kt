package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeDeleteRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeUpdateRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeDeleteResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeUpdateResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ChallengeService {
    @PATCH("challenge/update")
    suspend fun fetchChallengeUpdate(
        @Body request: CreateChallengeUpdateRequestDto
    ): BaseResponse<CreateChallengeUpdateResponseDto>

    @DELETE("challenge/delete")
    suspend fun fetchChallengeDelete(
        @Body request: CreateChallengeDeleteRequestDto
    ): BaseResponse<CreateChallengeDeleteResponseDto>

    @POST("challenge/location_logic/test")
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(
        @Body request: List<CreateLocationLogicTestRequestDto>
    ): BaseResponse<List<CreateLocationLogicTestResponseDto>>

    @POST("challenge/location_challenge/create")
    suspend fun fetchChallegeLocationChallengeCreate(
        @Body request: CreateLocationChallengeRequestDto
    ): BaseResponse<CreateLocationChallengeResponseDto>

    /*@POST("challenge/weekly_challenge/create")
    suspend fun fetchSweepWeeklyChallengeCreate(
        @Body request: CreateWeeklyChallengeRequestDto
    ): BaseResponse<CreateWeeklyChallengeResponseDto>*/
}