package com.umc.sweepic.domain.model.response.challenge

import java.time.Instant

data class CreateLocationLogicTestResponseModel (
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    val location: String
)