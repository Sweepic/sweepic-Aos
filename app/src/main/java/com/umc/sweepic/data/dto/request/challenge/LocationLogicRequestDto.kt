package com.umc.sweepic.data.dto.request.challenge

data class LocationLogicRequestDto(
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val id: String
)