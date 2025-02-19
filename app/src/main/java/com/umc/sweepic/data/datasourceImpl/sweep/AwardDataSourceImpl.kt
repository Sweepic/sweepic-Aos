package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.AwardDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.ImageIdCheckRequestDto
import com.umc.sweepic.data.dto.request.ModifyAwardRequestDto
import com.umc.sweepic.data.dto.response.CreateAwardResponseDto
import com.umc.sweepic.data.dto.response.ImageIdCheckResponseDto
import com.umc.sweepic.data.dto.response.ModifyAwardResponseDto
import com.umc.sweepic.data.dto.response.GetAwardResponseDto
import com.umc.sweepic.data.service.AwardService
import javax.inject.Inject

class AwardDataSourceImpl @Inject constructor(
    private val awardService: AwardService
): AwardDataSource {
    override suspend fun imageIdCheck(request: ImageIdCheckRequestDto): BaseResponse<ImageIdCheckResponseDto> =
        awardService.imageIdCheck(request)

    override suspend fun createAward(): BaseResponse<CreateAwardResponseDto> =
        awardService.createAward()

    override suspend fun modifyAward(
        awardId: String,
        request: List<ModifyAwardRequestDto>  // ✅ 인터페이스와 동일하게 수정
    ): BaseResponse<List<ModifyAwardResponseDto>> {
        return awardService.modifyAward(awardId, request)
    }

    override suspend fun getAwards(): BaseResponse<List<GetAwardResponseDto>> =
        awardService.getAwards()

}
