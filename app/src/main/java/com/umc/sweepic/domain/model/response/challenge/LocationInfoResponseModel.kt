package com.umc.sweepic.domain.model.response.challenge

data class LocationInfoResponseModel(
    val id: String,
    val displayName: String,
    val longitude: Double,
    val latitude: Double,
    val location: String,
    val timestamp: String
)