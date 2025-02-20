package com.umc.sweepic.data.repositoryImpl.challenge

import com.umc.sweepic.data.service.ChallengeService
import com.umc.sweepic.domain.model.request.challenge.GoalChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationInfoRequestModel
import com.umc.sweepic.domain.model.request.challenge.WeekChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.CommonChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GoalChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val challengeService: ChallengeService
): ChallengeRepository {
    override suspend fun fetchCreateWeekChallenge(request: WeekChallengeRequestModel): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchCreateWeekChallenge(request.toWeekChallengeRequestDto()).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchGetWeekChallenge(id: String): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchGetWeekChallenge(id).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchCreateLocationChallenge(request: LocationChallengeRequestModel): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchCreateLocationChallenge(request.toLocationChallengeRequestDto()).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchGetLocationChallenge(id: String): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchGetLocationChallenge(id).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchObserveLocationChallenge(request: List<LocationInfoRequestModel>): Result<List<LocationInfoResponseModel>> = runCatching {
        challengeService.fetchObserveLocationChallenge(request.map { it.toLocationInfoRequestDto() }).success.map { it.toLocationInfoResponseModel() }
    }

    override suspend fun fetchEditChallenge(request: GoalChallengeRequestModel): Result<GoalChallengeResponseModel> = runCatching {
        challengeService.fetchEditChallenge(request.toGoalChallengeRequestDto()).success.toGoalChallengeResponseModel()
    }

    override suspend fun fetchDeleteChallenge(id: String): Result<String> = runCatching {
        challengeService.fetchDeleteChallenge(id).success
    }

    override suspend fun fetchAcceptChallenge(id: String): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchAcceptChallenge(id).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchCompleteChallenge(id: String): Result<CommonChallengeResponseModel> = runCatching {
        challengeService.fetchCompleteChallenge(id).success.toCommonChallengeResponseModel()
    }

    override suspend fun fetchGetChallenge(): Result<List<GetChallengeResponseModel>> = runCatching {
        challengeService.fetchGetChallenge().success.map { it.toGetChallengeResponseModel() }
    }

    override suspend fun fetchGeoCode(hashedLocation: String): Result<String> = runCatching {
        challengeService.fetchGeoCode(hashedLocation).success
    }

    override suspend fun fetchUploadChallengeImage(challengeId: String, request: List<String>): Result<String> = runCatching {
        challengeService.fetchUploadChallengeImage(challengeId, request).success
    }
}