package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.RecordMemoListModel
import com.umc.sweepic.domain.model.response.sweep.GetMostTaggedModel

data class GetMostTaggedResponseDto(
    val success: List<MostTaggedItemDto>
) {
    data class MostTaggedItemDto(
        val _count: CountDto,
        val tagCategoryId: String,
        val content: String
    ) {
        data class CountDto (
            val _all : Int
        )

        fun toMostTaggedModel () =
            GetMostTaggedModel.MostTaggedItem(
                _count = GetMostTaggedModel.MostTaggedItem.CountModel(_count._all),
                tagCategoryId,
                content
            )
    }
    fun toGetMostTaggedModel() =
        GetMostTaggedModel(
            tags = success.map { it.toMostTaggedModel() }
        )
}