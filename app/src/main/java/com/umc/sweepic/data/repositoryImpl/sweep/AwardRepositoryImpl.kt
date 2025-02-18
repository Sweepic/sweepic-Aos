package com.umc.sweepic.data.repositoryImpl.sweep

import com.umc.sweepic.data.datasource.AwardDataSource
import com.umc.sweepic.data.dto.response.toModifyAwardResponseModel
import com.umc.sweepic.domain.model.request.award.ImageIdCheckRequestModel
import com.umc.sweepic.domain.model.request.award.ModifyAwardRequestModel
import com.umc.sweepic.domain.model.response.award.CreateAwardResponseModel
import com.umc.sweepic.domain.model.response.award.GetAwardResponseModel
import com.umc.sweepic.domain.model.response.award.ImageIdCheckResponseModel
import com.umc.sweepic.domain.model.response.award.ModifyAwardResponseModel
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import javax.inject.Inject

class AwardRepositoryImpl @Inject constructor(
    private val awardDataSource: AwardDataSource
): AwardRepository{
    override suspend fun imageIdCheck(request: ImageIdCheckRequestModel): Result<ImageIdCheckResponseModel> = runCatching {
        awardDataSource.imageIdCheck(request.toImageIdCheckRequestDto()).success.toImageIdCheckResponseModel()
    }

    override suspend fun createAward(): Result<CreateAwardResponseModel> = runCatching {
        awardDataSource.createAward().success.toCreateAwardResponseModel()
    }

    override suspend fun modifyAward(
        awardId: String,
        request: List<ModifyAwardRequestModel>
    ): Result<List<ModifyAwardResponseModel>> = runCatching {
        awardDataSource.modifyAward(
            awardId,
            request.map { it.toModifyAwardRequestDto() }
        ).success.toModifyAwardResponseModel()
    }

    override suspend fun getAwards(): Result<List<GetAwardResponseModel>> = runCatching {
        awardDataSource.getAwards().success.map { it.toGetAwardResponseModel() }  // ✅ 올바른 변환 함수 사용
    }

}