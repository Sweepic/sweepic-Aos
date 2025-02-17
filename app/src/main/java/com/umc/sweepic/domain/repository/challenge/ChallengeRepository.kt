package com.umc.sweepic.domain.repository.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateWeeklyChallengeRequestDto
import com.umc.sweepic.data.dto.response.challenge.AcceptChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CompleteChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetUserChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.GetWeeklyChallengeResponseDto
import com.umc.sweepic.domain.model.request.challenge.UpdateChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationLogicRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.AcceptChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.UpdateChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationLogicResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CompleteChallengeResponsModel
import com.umc.sweepic.domain.model.response.challenge.DeleteChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetUserChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetWeeklyChallengeResponseModel

interface ChallengeRepository {
    //날짜
    suspend fun createWeeklyChallenge(request: CreateWeeklyChallengeRequestModel): Result<CreateWeeklyChallengeResponseModel>
    suspend fun getWeeklyChallenge(id: String): Result<GetWeeklyChallengeResponseModel>

    //위치
    suspend fun createLocationChallenge(request: CreateLocationChallengeRequestModel): Result<CreateLocationChallengeResponseModel>
    suspend fun getLocationChallenge(id: String): Result<GetLocationChallengeResponseModel>
    suspend fun getLocationLogic(request: List<LocationLogicRequestModel>): Result<List<LocationLogicResponseModel>>

    //컨트롤
    suspend fun updateChallenge(request:UpdateChallengeRequestModel): Result<UpdateChallengeResponseModel>
    suspend fun deleteChallenge(id: String): Result<DeleteChallengeResponseModel>
    suspend fun acceptChallenge(id: String): Result<AcceptChallengeResponseModel>
    suspend fun completeChallenge(id: String): Result<CompleteChallengeResponsModel>
    suspend fun getUserChallenge(): Result<List<GetUserChallengeResponseModel>>

}