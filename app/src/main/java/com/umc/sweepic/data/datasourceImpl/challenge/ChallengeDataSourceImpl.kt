package com.umc.sweepic.data.datasourceImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.GoalChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationInfoRequestDto
import com.umc.sweepic.data.dto.request.challenge.WeekChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CommonChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GoalChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.LocationInfoResponseDto
import com.umc.sweepic.data.service.ChallengeService
import javax.inject.Inject

class ChallengeDataSourceImpl @Inject constructor(
    private val challengeService: ChallengeService
): ChallengeDataSource {
    override suspend fun fetchCreateWeekChallenge(request: WeekChallengeRequestDto): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchCreateWeekChallenge(request)

    override suspend fun fetchGetWeekChallenge(id: String): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchGetWeekChallenge(id)

    override suspend fun fetchCreateLocationChallenge(request: LocationChallengeRequestDto): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchCreateLocationChallenge(request)

    override suspend fun fetchGetLocationChallenge(id: String): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchGetLocationChallenge(id)

    override suspend fun fetchObserveLocationChallenge(request: List<LocationInfoRequestDto>): BaseResponse<List<LocationInfoResponseDto>> =
        challengeService.fetchObserveLocationChallenge(request)

    override suspend fun fetchEditChallenge(request: GoalChallengeRequestDto): BaseResponse<GoalChallengeResponseDto> =
        challengeService.fetchEditChallenge(request)

    override suspend fun fetchDeleteChallenge(id: String): BaseResponse<String> =
        challengeService.fetchDeleteChallenge(id)

    override suspend fun fetchAcceptChallenge(id: String): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchAcceptChallenge(id)

    override suspend fun fetchCompleteChallenge(id: String): BaseResponse<CommonChallengeResponseDto> =
        challengeService.fetchCompleteChallenge(id)

    override suspend fun fetchGetChallenge(): BaseResponse<List<GetChallengeResponseDto>> =
        challengeService.fetchGetChallenge()

    override suspend fun fetchGeoCode(hashedLocation: String): BaseResponse<String> =
        challengeService.fetchGeoCode(hashedLocation)

}