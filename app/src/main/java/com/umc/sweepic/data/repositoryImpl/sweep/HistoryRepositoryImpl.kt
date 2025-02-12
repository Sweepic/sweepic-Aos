package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.HistoryDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.domain.model.response.GetTagsByDateModel
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel
import com.umc.sweepic.domain.repository.sweep.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {

    override suspend fun getMostTagged(): Result<GetMostTaggedModel> = runCatching {
        val response = historyDataSource.getMostTagged()
        Log.d("HistoryRepositoryImpl", "API 응답 수신: $response")
        response.success.toMostTaggedModel()

    }.onFailure { error ->
        Log.e("HistoryRepositoryImpl", "API 호출 실패: ${error.message}")
    }

    override suspend fun getTagsByDate(
        year: Double,
        month: Double,
        date: Double?
    ): Result<GetTagsByDateModel> = runCatching {
        val response = historyDataSource.getTagsByDate(year, month, date)
        response.success.toDomainModel()

    }.onFailure { error ->
        Log.e("HistoryRepositoryImpl", "API 호출 실패: ${error.message}")
    }
}