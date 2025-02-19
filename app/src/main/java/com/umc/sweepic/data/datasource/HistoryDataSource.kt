package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto

interface HistoryDataSource {
    suspend fun getMostTagged(year: Double, month: Double): BaseResponse<List<GetMostTaggedResponseDto>>
}