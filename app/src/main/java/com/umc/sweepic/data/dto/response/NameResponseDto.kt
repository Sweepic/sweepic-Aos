package com.umc.sweepic.data.dto.response

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.domain.model.response.sweep.UpdateUserNameResponseModel

data class NameResponseDto(
    val id: Int,
    val name: String,
    val updatedAt: String
)

fun BaseResponse<NameResponseDto>.toModel(): UpdateUserNameResponseModel? {
    return success?.let {
        UpdateUserNameResponseModel(
            id = it.id,
            name = it.name,
            updatedAt = it.updatedAt
        )
    }
}
