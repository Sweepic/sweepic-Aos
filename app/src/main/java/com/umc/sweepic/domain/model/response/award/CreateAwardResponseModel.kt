package com.umc.sweepic.domain.model.response.award

data class CreateAwardResponseModel(
    val id: String,
    val awardMonth: String,
    val createdAt: String?,
    val updatedAt: String?,
    val status: Int,
    val userId: String
)
