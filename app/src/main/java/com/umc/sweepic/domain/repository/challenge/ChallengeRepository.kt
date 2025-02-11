package com.umc.sweepic.domain.repository.challenge

import com.umc.sweepic.domain.model.request.challenge.CreateChallengeDeleteRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateChallengeUpdateRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeDeleteResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel

interface ChallengeRepository {
    suspend fun fetchChallengeUpdate(request: CreateChallengeUpdateRequestModel): Result<CreateChallengeUpdateResponseModel>
    suspend fun fetchChallengeGet(): Result<List<ChallengeGetResponseModel>>
    suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestModel>): Result<List<CreateLocationLogicTestResponseModel>>
    suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel>
    suspend fun fetchWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel>
}