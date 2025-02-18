package com.umc.sweepic.data.dto.request.sweep

data class TagRequestDto(
    val tags: List<TagContentDto>
){
    data class TagContentDto(
        val tagCategoryId: String,
        val content: String
    )
}
