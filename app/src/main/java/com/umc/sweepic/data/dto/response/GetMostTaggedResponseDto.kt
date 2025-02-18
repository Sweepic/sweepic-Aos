package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel

data class GetMostTaggedResponseDto(
    val _count: CountDto,
    val tagCategoryId: String,
    val content: String
) {
    data class CountDto(
        val _all: Int
    ) {
        fun toCountModel() =
            GetMostTaggedModel.MostTaggedItem.CountModel(_all)
    }
    fun toMostTaggedModel() =
        GetMostTaggedModel.MostTaggedItem(
            _count.toCountModel(),
            tagCategoryId,
            content
        )
}
