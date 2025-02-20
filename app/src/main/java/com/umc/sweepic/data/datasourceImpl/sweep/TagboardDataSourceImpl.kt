package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.TagboardDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.DateTagsResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto
import com.umc.sweepic.data.service.TagboardService
import javax.inject.Inject

class TagboardDataSourceImpl @Inject constructor(
  private val tagboardService: TagboardService
): TagboardDataSource{
    override suspend fun getImageTags(mediaId: Double): BaseResponse<TagInfoResponseDto> =
        tagboardService.getImageTags(mediaId)

    override suspend fun getDateTags(
        year: Double,
        month: Double,
        date: Double
    ): BaseResponse<DateTagsResponseDto> =
        tagboardService.getDateTags(year,month, date)

}