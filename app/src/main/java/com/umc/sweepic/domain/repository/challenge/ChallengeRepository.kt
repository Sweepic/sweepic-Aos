package com.umc.sweepic.domain.repository.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel

interface ChallengeRepository {
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestModel>): Result<List<CreateLocationLogicTestResponseModel>>
    suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel>
//    suspend fun fetchSweepWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel>

}