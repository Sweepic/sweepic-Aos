package com.umc.sweepic.data.datasourceImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeUpdateRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeUpdateResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateWeeklyChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.ChallengeGetResponseDto
import com.umc.sweepic.data.service.ChallengeService
import javax.inject.Inject

class ChallengeDataSourceImpl @Inject constructor(
    private val challengeService: ChallengeService
): ChallengeDataSource {
    override suspend fun fetchChallengeUpdate(request: CreateChallengeUpdateRequestDto): BaseResponse<CreateChallengeUpdateResponseDto> =
        challengeService.fetchChallengeUpdate(request)

    override suspend fun fetchChallengeGet(): BaseResponse<List<ChallengeGetResponseDto>> =
        challengeService.fetchChallengeGet()

    override suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestDto>): BaseResponse<List<CreateLocationLogicTestResponseDto>> =
        challengeService.fetchChallengeLocationLogicTestChallengeCreate(request)

    override suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestDto): BaseResponse<CreateLocationChallengeResponseDto> =
        challengeService.fetchChallengeLocationChallengeCreate(request)

    override suspend fun fetchWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestDto): BaseResponse<CreateWeeklyChallengeResponseDto> =
        challengeService.fetchSweepWeeklyChallengeCreate(request)


}
