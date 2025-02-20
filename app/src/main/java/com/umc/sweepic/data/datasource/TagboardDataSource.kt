package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.DateTagsResponseDto
import com.umc.sweepic.data.dto.response.sweep.TagInfoResponseDto

interface TagboardDataSource {
    suspend fun getImageTags(mediaId: Double): BaseResponse<TagInfoResponseDto>
    suspend fun getDateTags(year: Double, month: Double, date: Double): BaseResponse<DateTagsResponseDto>
}