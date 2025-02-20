package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.DateTagsResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TagboardService {
    @GET("/tags/images/{mediaId}")
    suspend fun getImageTags(
        @Path("mediaId") mediaId: Double  // ✅ API 명세에 맞춰 'Double' 타입 사용
    ): BaseResponse<TagInfoResponseDto>

    @GET("tags/date")
    suspend fun getDateTags(
        @Query("year") year: Double,
        @Query("month") month: Double,
        @Query("date") date: Double
    ): BaseResponse<DateTagsResponseDto>
}