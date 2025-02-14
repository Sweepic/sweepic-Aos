package com.umc.sweepic.domain.model.response.sweep

import com.umc.sweepic.data.dto.response.GetMostTaggedResponseDto.MostTaggedItemDto

data class GetMostTaggedModel(
    val tags: List<MostTaggedItem>
) {
    data class MostTaggedItem(
        val _count: CountModel,
        val tagCategoryId: String,
        val content: String
    ) {
        data class CountModel(
            val _all: Int
        )
    }
}
