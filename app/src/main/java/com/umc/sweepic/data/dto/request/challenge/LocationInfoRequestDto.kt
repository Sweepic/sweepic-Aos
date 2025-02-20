package com.umc.sweepic.data.dto.request.challenge

data class LocationInfoRequestDto(
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val id: String
)
