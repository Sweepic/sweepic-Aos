package com.umc.sweepic.domain.repository.challenge

import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel

interface ChallengeRepository {
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: CreateLocationLogicTestRequestModel): Result<CreateLocationLogicTestResponseModel>
//    suspend fun fetchSweepLocationChallengeCreate(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel>
//    suspend fun fetchSweepWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel>

}