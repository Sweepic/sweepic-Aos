package com.umc.sweepic.domain.model.response.sweep

data class TagInfoResponseModel(
    val tags: List<TagModel>
) {
    data class TagModel(
        val tagCategory: TagCategoryModel,
        val content: String
    ) {
        data class TagCategoryModel(
            val tagType: String,
            val id: String
        )
    }
}