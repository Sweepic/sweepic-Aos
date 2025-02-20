package com.umc.sweepic.domain.model.response.sweep

data class GetUserInformationResponseModel(
    val id: Long,
    val email: String,
    val name: String,
    val goalCount: Int,
    val createdAt: String,
    val updatedAt: String
)
