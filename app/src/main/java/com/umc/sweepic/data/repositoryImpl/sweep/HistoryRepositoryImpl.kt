package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.HistoryDataSource
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel
import com.umc.sweepic.domain.repository.sweep.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {

    override suspend fun getMostTagged(year: Double, month: Double): Result<GetMostTaggedModel> = runCatching {
        Log.d("HistoryRepositoryImpl", "Fetching most tagged data for Year: $year, Month: $month")

        val response = historyDataSource.getMostTagged(year, month)
        val mappedData = response.success.map { it.toMostTaggedModel() }

        Log.d("HistoryRepositoryImpl", "Success: Received ${mappedData.size} tags for Month: $month")

        GetMostTaggedModel(mappedData)
    }.onFailure { error ->
        Log.e("HistoryRepositoryImpl", "Error fetching most tagged data for Month: $month - ${error.message}")
    }
}
