package com.umc.sweepic.domain.repository.challenge

import com.umc.sweepic.domain.model.request.challenge.GoalChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationInfoRequestModel
import com.umc.sweepic.domain.model.request.challenge.WeekChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.CommonChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GoalChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel

interface ChallengeRepository {
    // Date-Challenge
    suspend fun fetchCreateWeekChallenge(request: WeekChallengeRequestModel): Result<CommonChallengeResponseModel>
    suspend fun fetchGetWeekChallenge(id: String): Result<CommonChallengeResponseModel>

    // Location-Challenge
    suspend fun fetchCreateLocationChallenge(request: LocationChallengeRequestModel): Result<CommonChallengeResponseModel>
    suspend fun fetchGetLocationChallenge(id: String): Result<CommonChallengeResponseModel>
    suspend fun fetchObserveLocationChallenge(request: List<LocationInfoRequestModel>): Result<List<LocationInfoResponseModel>>

    // Challenge
    suspend fun fetchEditChallenge(request: GoalChallengeRequestModel): Result<GoalChallengeResponseModel>
    suspend fun fetchDeleteChallenge(id: String): Result<String>
    suspend fun fetchAcceptChallenge(id: String): Result<CommonChallengeResponseModel>
    suspend fun fetchCompleteChallenge(id: String): Result<CommonChallengeResponseModel>
    suspend fun fetchGetChallenge(): Result<CommonChallengeResponseModel>
    suspend fun fetchGeoCode(hashedLocation: String): Result<String>
}