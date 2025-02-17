package com.umc.sweepic.data.datasource.challenge

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.UpdateChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationLogicRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.AcceptChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CompleteChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.UpdateChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.LocationLogicResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateWeeklyChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.DeleteChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetUserChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetWeeklyChallengeResponseDto

interface ChallengeDataSource {
    //날짜
    suspend fun createWeeklyChallenge(requestDto: CreateWeeklyChallengeRequestDto): BaseResponse<CreateWeeklyChallengeResponseDto>
    suspend fun getWeeklyChallenge(id: String): BaseResponse<GetWeeklyChallengeResponseDto>

    //위치
    suspend fun createLocationChallenge(request: CreateLocationChallengeRequestDto): BaseResponse<CreateLocationChallengeResponseDto>
    suspend fun getLocationChallenge(id: String): BaseResponse<GetLocationChallengeResponseDto>
    suspend fun getLocationLogic(request: List<LocationLogicRequestDto>): BaseResponse<List<LocationLogicResponseDto>>

    //컨트롤
    suspend fun updateChallenge(request: UpdateChallengeRequestDto): BaseResponse<UpdateChallengeResponseDto>
    suspend fun deleteChallenge(id: String): BaseResponse<DeleteChallengeResponseDto>
    suspend fun acceptChallenge(id: String): BaseResponse<AcceptChallengeResponseDto>
    suspend fun completeChallenge(id: String): BaseResponse<CompleteChallengeResponseDto>
    suspend fun getUserChallenge(): BaseResponse<List<GetUserChallengeResponseDto>>
}
