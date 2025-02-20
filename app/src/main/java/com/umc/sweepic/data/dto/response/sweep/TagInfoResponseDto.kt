package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.TagInfoResponseModel

data class TagInfoResponseDto(
    val tags: List<TagDto>
) {
    data class TagDto(
        val tagCategory: TagCategoryDto,
        val content: String
    ) {
        data class TagCategoryDto(
            val tagType: String,
            val id: String
        ) {
            fun toTagCategoryModel() =
                TagInfoResponseModel.TagModel.TagCategoryModel(tagType, id)
        }
        fun toTagModel() =
            TagInfoResponseModel.TagModel(tagCategory.toTagCategoryModel(), content)
    }
    fun toTagInfoResponseModel() =
        TagInfoResponseModel(tags.map { it.toTagModel() })
}
