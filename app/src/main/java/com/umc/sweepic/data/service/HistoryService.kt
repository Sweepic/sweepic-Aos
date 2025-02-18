package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryService {
    @GET("user/history/most_tagged/get")
    suspend fun getMostTagged() : BaseResponse<List<GetMostTaggedResponseDto>>

}