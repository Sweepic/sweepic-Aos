package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.ImageIdCheckRequestDto
import com.umc.sweepic.data.dto.request.ModifyAwardRequestDto
import com.umc.sweepic.data.dto.response.CreateAwardResponseDto
import com.umc.sweepic.data.dto.response.ImageIdCheckResponseDto
import com.umc.sweepic.data.dto.response.ModifyAwardResponseDto
import com.umc.sweepic.data.dto.response.GetAwardResponseDto

interface AwardDataSource {
    suspend fun imageIdCheck(request: ImageIdCheckRequestDto): BaseResponse<ImageIdCheckResponseDto>
    suspend fun createAward(): BaseResponse<CreateAwardResponseDto>
    suspend fun modifyAward(awardId: String, request: List<ModifyAwardRequestDto>
    ): BaseResponse<List<ModifyAwardResponseDto>>
    suspend fun getAwards(): BaseResponse<List<GetAwardResponseDto>>
}