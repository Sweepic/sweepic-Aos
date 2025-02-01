package com.umc.sweepic.data.repositoryImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository

import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource
): ChallengeRepository {
    override suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: CreateLocationLogicTestRequestModel): Result<CreateLocationLogicTestResponseModel> = runCatching {
        challengeDataSource.fetchChallengeLocationLogicTestChallengeCreate(request.toCreateLocationLogicTestRequestDto()).success.toCreateLocationLogicTestResponseModel()
    }

}