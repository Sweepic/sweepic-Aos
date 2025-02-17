package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.UpdateChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationLogicRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.AcceptChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.DeleteChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.UpdateChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.LocationLogicResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateWeeklyChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetUserChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CompleteChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetWeeklyChallengeResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeService {
    //날짜 기반 챌린지
    @POST("challenge/weekly_challenge/create")
    suspend fun createWeeklyChallenge(
        @Body request: CreateWeeklyChallengeRequestDto
    ): BaseResponse<CreateWeeklyChallengeResponseDto>

    @GET("challenge/weekly_challenge/get/{id}")
    suspend fun getWeeklyChallenge(
        @Path("id") id:String
    ): BaseResponse<GetWeeklyChallengeResponseDto>

    //위치 기반 챌린지
    @POST("challenge/location_logic/test")
    suspend fun getLocationLogic(
        @Body request: List<LocationLogicRequestDto>
    ): BaseResponse<List<LocationLogicResponseDto>>

    @GET("challenge/location_challenge/get/{id}")
    suspend fun getLocationChallenge(
        @Path("id") id:String
    ): BaseResponse<GetLocationChallengeResponseDto>

    @POST("challenge/location_challenge/create")
    suspend fun createLocationChallenge(
        @Body request: CreateLocationChallengeRequestDto
    ): BaseResponse<CreateLocationChallengeResponseDto>

    //챌린지 컨트롤
    @PATCH("challenge/update")
    suspend fun updateChallenge(
        @Body request: UpdateChallengeRequestDto
    ): BaseResponse<UpdateChallengeResponseDto>

    @DELETE("challenge/delete")
    suspend fun deleteChallenge(
        @Path("id") id:String,
    ): BaseResponse<DeleteChallengeResponseDto>

    @PATCH("challenge/accept/{id}")
    suspend fun acceptChallenge(
        @Path("id") id:String
    ): BaseResponse<AcceptChallengeResponseDto>

    @PATCH("challenge/complete/{id}")
    suspend fun completeChallenge(
        @Path("id") id:String
    ): BaseResponse<CompleteChallengeResponseDto>

    @GET("challenge/get")
    suspend fun getUserChallenge(
    ): BaseResponse<List<GetUserChallengeResponseDto>>








}