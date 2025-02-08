package com.umc.sweepic.data.datasourceImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto
import com.umc.sweepic.data.service.ChallengeService
import javax.inject.Inject

class ChallengeDataSourceImpl @Inject constructor(
    private val challengeService: ChallengeService
): ChallengeDataSource {
    override suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestDto>): BaseResponse<List<CreateLocationLogicTestResponseDto>> =
        challengeService.fetchChallengeLocationLogicTestChallengeCreate(request)

    override suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestDto): BaseResponse<CreateLocationChallengeResponseDto> =
        challengeService.fetchChallegeLocationChallengeCreate(request)
}
