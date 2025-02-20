package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryService {
    @GET("/user/history/most_tagged/get/{year}/{month}")
    suspend fun getMostTagged(
        @Path("year") year : Double,
        @Path("month") month : Double,
    ) : BaseResponse<List<GetMostTaggedResponseDto>>

}