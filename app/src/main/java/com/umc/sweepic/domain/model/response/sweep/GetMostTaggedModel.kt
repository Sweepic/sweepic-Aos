package com.umc.sweepic.domain.model.response.sweep

data class GetMostTaggedModel(
    val tags: List<MostTaggedItem>
) {
    data class MostTaggedItem(
        val tagCategoryId: String,
        val content: String
    )
}
