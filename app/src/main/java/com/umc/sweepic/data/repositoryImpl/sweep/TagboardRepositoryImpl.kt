package com.umc.sweepic.data.repositoryImpl.sweep

import com.umc.sweepic.data.datasource.TagboardDataSource
import com.umc.sweepic.domain.model.response.sweep.DateTagsResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel
import com.umc.sweepic.domain.repository.sweep.TagboardRepository
import javax.inject.Inject

class TagboardRepositoryImpl @Inject constructor(
    private val tagboardDataSource: TagboardDataSource
): TagboardRepository{
    override suspend fun getImageTags(mediaId: Double): Result<TagInfoResponseModel> = runCatching {
        tagboardDataSource.getImageTags(mediaId).success.toTagInfoResponseModel()
    }

    override suspend fun getDateTags(year: Double, month: Double, date: Double): Result<DateTagsResponseModel> = runCatching {
        tagboardDataSource.getDateTags(year, month, date).success.toDateTagsResponseModel()
    }
}
