package com.umc.sweepic.domain.model.response.sweep

data class GetMostTaggedModel(
    val success: List<MostTaggedItem>
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
