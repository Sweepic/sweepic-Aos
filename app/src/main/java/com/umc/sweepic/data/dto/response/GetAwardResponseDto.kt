package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.award.GetAwardResponseModel

data class GetAwardResponseDto(
    val id: String,
    val awardMonth: String,
    val createdAt: String,
    val updatedAt: String,
    val status: Int,
    val userId: String
){
    fun toGetAwardResponseModel(): GetAwardResponseModel {
        return GetAwardResponseModel(id, awardMonth, createdAt, updatedAt, status, userId)
    }
}
