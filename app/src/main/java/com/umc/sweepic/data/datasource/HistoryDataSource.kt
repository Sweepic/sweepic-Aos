package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto
import com.umc.sweepic.data.dto.response.GetTagsByDateResponseDto

interface HistoryDataSource {
    suspend fun getMostTagged(): BaseResponse<GetMostTaggedResponseDto>
    suspend fun getTagsByDate(year: Double, month: Double, date: Double?): BaseResponse<GetTagsByDateResponseDto>
}