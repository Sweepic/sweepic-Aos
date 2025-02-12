package com.umc.sweepic.domain.model.response

data class GetTagsByDateModel(
    val tags: List<TagsByDateItem>
) {
    data class TagsByDateItem(
        val tagCategoryId: String,
        val content: String,
        val count: Int
    )
}
