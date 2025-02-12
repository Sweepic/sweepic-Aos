package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.GetTagsByDateModel

data class GetTagsByDateResponseDto(
    val tags: List<String>
) {
    fun toDomainModel(): GetTagsByDateModel {
        val tagItems = tags.map { tag ->
            GetTagsByDateModel.TagsByDateItem(
                //기본값 설정해두기
                tagCategoryId = "UNKNOWN",
                content = tag,
                count = 1
            )
        }
        return GetTagsByDateModel(tagItems)
    }
}
