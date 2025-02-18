package com.umc.sweepic.domain.model.response.sweep

data class TagResponseModel(
    val tags: List<TagInfoModel>
) {
    data class TagInfoModel(
        val tagId: String,
        val imageId: String,
        val status: Int,
        val updatedAt: String,
        val createdAt: String,
        val id: String
    )
}
