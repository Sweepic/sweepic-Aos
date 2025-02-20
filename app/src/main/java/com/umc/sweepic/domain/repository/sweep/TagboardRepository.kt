package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.response.sweep.DateTagsResponseModel
import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel

interface TagboardRepository {
    suspend fun getImageTags(mediaId: Double): Result<TagInfoResponseModel>
    suspend fun getDateTags(year: Double, month: Double, date: Double): Result<DateTagsResponseModel>
}