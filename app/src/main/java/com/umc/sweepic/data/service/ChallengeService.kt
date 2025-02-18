package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.GoalChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationInfoRequestDto
import com.umc.sweepic.data.dto.request.challenge.WeekChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CommonChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GoalChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.LocationInfoResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeService {
    // Date-Challenge
    @POST("challenge/weekly_challenge/create")
    suspend fun fetchCreateWeekChallenge(
        @Body request: WeekChallengeRequestDto
    ): BaseResponse<CommonChallengeResponseDto>

    @GET("challenge/weekly_challenge/get/{id}")
    suspend fun fetchGetWeekChallenge(
        @Path("id") id: String
    ): BaseResponse<CommonChallengeResponseDto>

    // Location-Challenge
    @POST("challenge/location_challenge/create")
    suspend fun fetchCreateLocationChallenge(
        @Body request: LocationChallengeRequestDto
    ): BaseResponse<CommonChallengeResponseDto>

    @GET("challenge/location_challenge/get/{id}")
    suspend fun fetchGetLocationChallenge(
        @Path("id") id: String
    ): BaseResponse<CommonChallengeResponseDto>

    @POST("challenge/location_logic/test")
    suspend fun fetchObserveLocationChallenge(
        @Body request: List<LocationInfoRequestDto>
    ): BaseResponse<List<LocationInfoResponseDto>>

    // Challenge
    @PATCH("challenge/update")
    suspend fun fetchEditChallenge(
        @Body request: GoalChallengeRequestDto
    ): BaseResponse<GoalChallengeResponseDto>

    @DELETE("challenge/delete/{id}")
    suspend fun fetchDeleteChallenge(
        @Path("id") id: String
    ): BaseResponse<String>

    @PATCH("challenge/accept/{id}")
    suspend fun fetchAcceptChallenge(
        @Path("id") id: String
    ): BaseResponse<CommonChallengeResponseDto>

    @PATCH("challenge/complete/{id}")
    suspend fun fetchCompleteChallenge(
        @Path("id") id: String
    ): BaseResponse<CommonChallengeResponseDto>

    @GET("challenge/get")
    suspend fun fetchGetChallenge(): BaseResponse<CommonChallengeResponseDto>

    @GET("challenge/getGeoCode")
    suspend fun fetchGeoCode(
        @Path("hashedLocation") hashedLocation: String
    ): BaseResponse<String>
}