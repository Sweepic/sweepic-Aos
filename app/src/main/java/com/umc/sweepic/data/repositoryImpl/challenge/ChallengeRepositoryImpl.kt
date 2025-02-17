package com.umc.sweepic.data.repositoryImpl.challenge

import com.umc.sweepic.data.datasource.challenge.ChallengeDataSource
import com.umc.sweepic.data.dto.response.challenge.GetUserChallengeResponseDto.Companion.toResponseModelList
import com.umc.sweepic.data.dto.response.challenge.LocationLogicResponseDto.Companion.toResponseModelList
import com.umc.sweepic.domain.model.request.challenge.UpdateChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationLogicRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationLogicRequestModel.Companion.toDtoList
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.AcceptChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CompleteChallengeResponsModel
import com.umc.sweepic.domain.model.response.challenge.UpdateChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationLogicResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.DeleteChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetUserChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetWeeklyChallengeResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository

import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource
): ChallengeRepository {
    //날짜 기반 챌린지
    override suspend fun createWeeklyChallenge(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel> =
        runCatching {
            challengeDataSource.createWeeklyChallenge(request.toCreateWeeklyChallengeRequestDto()).success.toCreateWeeklyChallengeResponseModel()
        }

    override suspend fun getWeeklyChallenge(id: String): Result<GetWeeklyChallengeResponseModel> =
        runCatching {
            challengeDataSource.getWeeklyChallenge(id).success.toGetWeeklyChallengeResponseModel()
        }

    // 위치 기반 챌린지
    override suspend fun createLocationChallenge(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel> =
        runCatching {
            challengeDataSource.createLocationChallenge(request.toCreateLocationChallengeRequestDto()).success.toCreateLocationChallengeResponseModel()
        }

    override suspend fun getLocationChallenge(id: String): Result<GetLocationChallengeResponseModel> =
        runCatching {
            challengeDataSource.getLocationChallenge(id).success.toGetLocationChallengeResponseModel()
        }

    override suspend fun getLocationLogic(request: List<LocationLogicRequestModel>): Result<List<LocationLogicResponseModel>> =
        runCatching {
            challengeDataSource.getLocationLogic(request.toDtoList()).success.toResponseModelList()
        }

    //컨트롤
    override suspend fun updateChallenge(request: UpdateChallengeRequestModel): Result<UpdateChallengeResponseModel> =
        runCatching {
            challengeDataSource.updateChallenge(request.toUpdateChallengeRequestDto()).success.toUpdateChallengeResponseModel()
        }

    override suspend fun deleteChallenge(id: String): Result<DeleteChallengeResponseModel> =
        runCatching {
            challengeDataSource.deleteChallenge(id).success.toDeleteChallengeResponseModel()
        }

    override suspend fun acceptChallenge(id: String): Result<AcceptChallengeResponseModel> =
        runCatching {
            challengeDataSource.acceptChallenge(id).success.toAcceptChallengeResponseModel()
        }

    override suspend fun completeChallenge(id: String): Result<CompleteChallengeResponsModel> =
        runCatching {
            challengeDataSource.completeChallenge(id).success.toCompleteChallengeResponseModel()
        }

    override suspend fun getUserChallenge(): Result<List<GetUserChallengeResponseModel>> =
        runCatching {
            challengeDataSource.getUserChallenge().success.toResponseModelList()
        }
}