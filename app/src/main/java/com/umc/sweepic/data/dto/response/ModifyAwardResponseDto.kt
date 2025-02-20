package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.response.award.ModifyAwardResponseModel

data class ModifyAwardResponseDto(
    val imageId: String,
    val createdAt: String,
    val updatedAt: String,  // 오타 수정
    val status: Int,
    val awardId: String
){
    fun toModifyAwardResponseModel(): ModifyAwardResponseModel {
        return ModifyAwardResponseModel(
            imageId, createdAt, updatedAt, status, awardId
        )
    }
}

fun List<ModifyAwardResponseDto>.toModifyAwardResponseModel(): List<ModifyAwardResponseModel> {
    return this.map { it.toModifyAwardResponseModel() }
}

