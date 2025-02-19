package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.award.CreateAwardResponseModel

data class CreateAwardResponseDto(
    val id: String,
    val awardMonth: String,
    val createdAt: String?,
    val updatedAt: String?,
    val status: Int,
    val userId: String
) {
    fun toCreateAwardResponseModel(): CreateAwardResponseModel {  // ✅ 반환값 추가
        return CreateAwardResponseModel(  // ✅ 반환하도록 수정
            id = id,
            awardMonth = awardMonth,
            createdAt = createdAt,
            updatedAt = updatedAt,  // ✅ 수정된 변수명 반영
            status = status,
            userId = userId
        )
    }
}
