package com.umc.sweepic.data.repositoryImpl.sweep

import com.umc.sweepic.data.datasource.HistoryDataSource
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel
import com.umc.sweepic.domain.repository.sweep.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {

    override suspend fun getMostTagged(): Result<GetMostTaggedModel> = runCatching {
        GetMostTaggedModel(historyDataSource.getMostTagged().success.map { it.toMostTaggedModel() })
    }
}