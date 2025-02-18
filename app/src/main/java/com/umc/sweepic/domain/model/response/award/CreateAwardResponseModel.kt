package com.umc.sweepic.domain.model.response.award

data class CreateAwardResponseModel(
    val id: String,
    val awardMonth: String,
    val createdAt: String,
    val updateAt: String,
    val userId: String
)
