package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.HistoryDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto
import com.umc.sweepic.data.dto.response.GetTagsByDateResponseDto
import com.umc.sweepic.data.service.HistoryService
import javax.inject.Inject

class HistoryDataSourceImpl @Inject constructor(
    private val historyService: HistoryService
) : HistoryDataSource {

    override suspend fun getMostTagged(): BaseResponse<GetMostTaggedResponseDto> =
        historyService.getMostTagged()

    override suspend fun getTagsByDate(
        year: Double,
        month: Double,
        date: Double?
    ): BaseResponse<GetTagsByDateResponseDto> =
        historyService.getTagsByDate(year, month, date)
}

