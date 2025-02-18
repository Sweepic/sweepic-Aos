package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.request.sweep.TagRequestModel
import com.umc.sweepic.domain.model.response.sweep.TagResponseModel
import dagger.multibindings.StringKey

data class TagResponseDto(
    val tags: List<TagInfoDto>
){
    data class TagInfoDto(
        val tagId: String,
        val imageId: String,
        val status: Int,
        val updatedAt: String,
        val createdAt: String,
        val id: String
    ) {
        fun toTagInfoModel() =
            TagResponseModel.TagInfoModel(tagId, imageId, status, updatedAt, createdAt, id)
    }
    fun toTagRequestModel() =
        TagResponseModel(tags.map { it.toTagInfoModel() })
}
