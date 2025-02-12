package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.response.GetTagsByDateModel
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel

interface HistoryRepository {
    suspend fun getMostTagged() : Result<GetMostTaggedModel>
    suspend fun getTagsByDate(year: Double, month: Double, date: Double?): Result<GetTagsByDateModel>
}