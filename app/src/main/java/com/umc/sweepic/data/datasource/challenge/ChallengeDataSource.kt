package com.umc.sweepic.data.datasource.challenge

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.GoalChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.LocationInfoRequestDto
import com.umc.sweepic.data.dto.request.challenge.WeekChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CommonChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GoalChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.LocationInfoResponseDto


interface ChallengeDataSource {
    // Date-Challenge
    suspend fun fetchCreateWeekChallenge(request: WeekChallengeRequestDto): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchGetWeekChallenge(id: String): BaseResponse<CommonChallengeResponseDto>

    // Location-Challenge
    suspend fun fetchCreateLocationChallenge(request: LocationChallengeRequestDto): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchGetLocationChallenge(id: String): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchObserveLocationChallenge(request: List<LocationInfoRequestDto>): BaseResponse<List<LocationInfoResponseDto>>

    // Challenge
    suspend fun fetchEditChallenge(request: GoalChallengeRequestDto): BaseResponse<GoalChallengeResponseDto>
    suspend fun fetchDeleteChallenge(id: String): BaseResponse<String>
    suspend fun fetchAcceptChallenge(id: String): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchCompleteChallenge(id: String): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchGetChallenge(): BaseResponse<CommonChallengeResponseDto>
    suspend fun fetchGeoCode(hashedLocation: String): BaseResponse<String>
}