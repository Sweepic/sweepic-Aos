package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeDeleteRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeUpdateRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeDeleteResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeUpdateResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateWeeklyChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.ChallengeGetResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeService {
    @PATCH("challenge/update")
    suspend fun fetchChallengeUpdate(
        @Body request: CreateChallengeUpdateRequestDto
    ): BaseResponse<CreateChallengeUpdateResponseDto>

    @GET("challenge/get/{userId}")
    suspend fun fetchChallengeGet(
        @Path("userId") userId: String
    ): BaseResponse<List<ChallengeGetResponseDto>>

    @DELETE("challenge/delete")
    suspend fun fetchChallengeDelete(
        @Body request: CreateChallengeDeleteRequestDto
    ): BaseResponse<CreateChallengeDeleteResponseDto>

    @POST("challenge/location_logic/test")
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(
        @Body request: List<CreateLocationLogicTestRequestDto>
    ): BaseResponse<List<CreateLocationLogicTestResponseDto>>

    @POST("challenge/location_challenge/create")
    suspend fun fetchChallengeLocationChallengeCreate(
        @Body request: CreateLocationChallengeRequestDto
    ): BaseResponse<CreateLocationChallengeResponseDto>

    @POST("challenge/weekly_challenge/create")
    suspend fun fetchSweepWeeklyChallengeCreate(
        @Body request: CreateWeeklyChallengeRequestDto
    ): BaseResponse<CreateWeeklyChallengeResponseDto>
}