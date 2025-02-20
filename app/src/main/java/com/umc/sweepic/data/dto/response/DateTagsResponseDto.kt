package com.umc.sweepic.data.dto.response

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.domain.model.response.sweep.DateTagsResponseModel

data class DateTagsResponseDto(
    val tags: List<String>
) {
    fun toDateTagsResponseModel(): DateTagsResponseModel {
        return DateTagsResponseModel(tags = tags)
    }
}
