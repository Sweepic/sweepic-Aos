package com.umc.sweepic.data.repositoryImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto.Companion.toResponseModelList
import com.umc.sweepic.domain.model.request.challenge.CreateChallengeUpdateRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel.Companion.toDtoList
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository

import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource
): ChallengeRepository {
    override suspend fun fetchChallengeUpdate(request: CreateChallengeUpdateRequestModel): Result<CreateChallengeUpdateResponseModel> = runCatching {
        challengeDataSource.fetchChallengeUpdate(request.toCreateChallengeUpdateRequestDto()).success.toCreateChallengeUpdateResponseModel()
    }

    override suspend fun fetchChallengeGet(userId: String): Result<List<ChallengeGetResponseModel>> = runCatching {
        challengeDataSource.fetchChallengeGet(userId).success.map { it.toChallengeGetResponseModel() }
    }

    override suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestModel>): Result<List<CreateLocationLogicTestResponseModel>> = runCatching {
        challengeDataSource.fetchChallengeLocationLogicTestChallengeCreate(request.toDtoList()).success.toResponseModelList()
    }

    override suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel> = runCatching {
        challengeDataSource.fetchChallengeLocationChallengeCreate(request.toCreateLocationChallengeRequestDto()).success.toCreateLocationChallengeResponseModel()
    }

    override suspend fun fetchWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel> = runCatching {
        challengeDataSource.fetchWeeklyChallengeCreate(request.toCreateWeeklyChallengeRequestDto()).success.toCreateWeeklyChallengeResponseModel()
    }
}