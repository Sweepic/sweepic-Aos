package com.umc.sweepic.data.dto.request.challenge

import java.time.Instant

data class CreateLocationLogicTestRequestDto(
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)
