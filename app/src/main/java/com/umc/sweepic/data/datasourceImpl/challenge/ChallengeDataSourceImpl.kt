package com.umc.sweepic.data.datasourceImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
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
import com.umc.sweepic.data.service.ChallengeService
import javax.inject.Inject

class ChallengeDataSourceImpl @Inject constructor(
    private val challengeService: ChallengeService
): ChallengeDataSource {
    //날짜
    override suspend fun createWeeklyChallenge(request: CreateWeeklyChallengeRequestDto): BaseResponse<CreateWeeklyChallengeResponseDto> =
        challengeService.createWeeklyChallenge(request)

    override suspend fun getWeeklyChallenge(id: String): BaseResponse<GetWeeklyChallengeResponseDto> =
        challengeService.getWeeklyChallenge(id)

    //위치
    override suspend fun createLocationChallenge(request: CreateLocationChallengeRequestDto): BaseResponse<CreateLocationChallengeResponseDto> =
        challengeService.createLocationChallenge(request)

    override suspend fun getLocationChallenge(id: String): BaseResponse<GetLocationChallengeResponseDto> =
        challengeService.getLocationChallenge(id)

    override suspend fun getLocationLogic(request: List<LocationLogicRequestDto>): BaseResponse<List<LocationLogicResponseDto>> =
        challengeService.getLocationLogic(request)

    //컨트롤
    override suspend fun updateChallenge(request: UpdateChallengeRequestDto): BaseResponse<UpdateChallengeResponseDto> =
        challengeService.updateChallenge(request)

    override suspend fun deleteChallenge(id: String): BaseResponse<DeleteChallengeResponseDto> =
        challengeService.deleteChallenge(id)

    override suspend fun acceptChallenge(id: String): BaseResponse<AcceptChallengeResponseDto> =
        challengeService.acceptChallenge(id)


    override suspend fun completeChallenge(id: String): BaseResponse<CompleteChallengeResponseDto> =
        challengeService.completeChallenge(id)

    override suspend fun getUserChallenge(): BaseResponse<List<GetUserChallengeResponseDto>> =
        challengeService.getUserChallenge()


}
