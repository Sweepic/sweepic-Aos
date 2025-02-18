package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.TagRequestDto
import java.io.Serializable

data class TagRequestModel(
    val tags: List<TagContentModel>
): Serializable {
    data class TagContentModel(
        val tagCategoryId: String,
        val content: String
    ) {
        fun toTagContentDto() =
            TagRequestDto.TagContentDto(tagCategoryId, content)
    }
    fun toTagRequestDto() =
        TagRequestDto(tags.map { it.toTagContentDto() })
}
