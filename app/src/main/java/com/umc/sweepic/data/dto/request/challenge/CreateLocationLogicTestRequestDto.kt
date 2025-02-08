package com.umc.sweepic.data.dto.request.challenge

data class CreateLocationLogicTestRequestDto(
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)