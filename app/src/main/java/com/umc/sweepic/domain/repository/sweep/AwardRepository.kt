package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.request.award.ImageIdCheckRequestModel
import com.umc.sweepic.domain.model.request.award.ModifyAwardRequestModel
import com.umc.sweepic.domain.model.response.award.CreateAwardResponseModel
import com.umc.sweepic.domain.model.response.award.GetAwardResponseModel
import com.umc.sweepic.domain.model.response.award.ImageIdCheckResponseModel
import com.umc.sweepic.domain.model.response.award.ModifyAwardResponseModel

interface AwardRepository {
    suspend fun imageIdCheck(request: ImageIdCheckRequestModel): Result<ImageIdCheckResponseModel>
    suspend fun createAward(): Result<CreateAwardResponseModel>
    suspend fun modifyAward(awardId: String, request: List<ModifyAwardRequestModel>): Result<List<ModifyAwardResponseModel>>
    suspend fun getAwards(): Result<List<GetAwardResponseModel>>
}