package com.umc.sweepic.data.repositoryImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto.Companion.toResponseModelList
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel.Companion.toDtoList
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository

import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource
): ChallengeRepository {
    override suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestModel>): Result<List<CreateLocationLogicTestResponseModel>> = runCatching {
        challengeDataSource.fetchChallengeLocationLogicTestChallengeCreate(request.toDtoList()).success.toResponseModelList()
    }

    override suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel> = runCatching {
        challengeDataSource.fetchChallengeLocationChallengeCreate(request.toCreateLocationChallengeRequestDto()).success.toCreateLocationChallengeResponseModel()
    }

}