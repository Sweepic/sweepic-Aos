package com.umc.sweepic.domain.model.response.challenge

data class LocationLogicResponseModel (
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val location: String,
    val timestamp: String
)