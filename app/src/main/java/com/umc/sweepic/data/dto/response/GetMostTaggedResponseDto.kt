package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel

data class GetMostTaggedResponseDto(
    val tags: List<MostTaggedItemDto>
) {
    fun toMostTaggedModel() = GetMostTaggedModel(tags.map { it.toMostTaggedItemModel() })
}

data class MostTaggedItemDto(
    val tagCategoryId: String,
    val content: String
) {
    fun toMostTaggedItemModel() = GetMostTaggedModel.MostTaggedItem(tagCategoryId, content)
}
