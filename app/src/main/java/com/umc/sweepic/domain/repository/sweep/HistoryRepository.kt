package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel

interface HistoryRepository {
    suspend fun getMostTagged(year: Double, month: Double) : Result<GetMostTaggedModel>
}