package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.ImageIdCheckRequestDto
import com.umc.sweepic.data.dto.request.ModifyAwardRequestDto
import com.umc.sweepic.data.dto.response.CreateAwardResponseDto
import com.umc.sweepic.data.dto.response.ImageIdCheckResponseDto
import com.umc.sweepic.data.dto.response.ModifyAwardResponseDto
import com.umc.sweepic.data.dto.response.GetAwardResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AwardService {
    @POST("/images")
    suspend fun imageIdCheck(
        @Body request: ImageIdCheckRequestDto
    ): BaseResponse<ImageIdCheckResponseDto>

    @POST("/user/history/award/create")
    suspend fun createAward(): BaseResponse<CreateAwardResponseDto>

    @PATCH("/user/history/award/modify")
    suspend fun modifyAward(
        @Query("awardId") awardId: String,
        @Body request: List<ModifyAwardRequestDto>): BaseResponse<List<ModifyAwardResponseDto>>

    @GET("/user/history/award/get")
    suspend fun getAwards(): BaseResponse<List<GetAwardResponseDto>>
}