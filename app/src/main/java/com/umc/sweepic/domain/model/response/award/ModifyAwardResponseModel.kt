package com.umc.sweepic.domain.model.response.award

data class ModifyAwardResponseModel(
    val imageId: String,
    val createdAt: String,
    val updateAt: String,
    val status: Int,
    val awardId: String
)
