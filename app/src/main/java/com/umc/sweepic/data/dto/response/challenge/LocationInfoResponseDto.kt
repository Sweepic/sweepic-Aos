package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel

data class LocationInfoResponseDto(
    val id: String,
    val displayName: String,
    val longitude: Double,
    val latitude: Double,
    val location: String,
    val timestamp: String
){
    fun toLocationInfoResponseModel() =
        LocationInfoResponseModel(id, displayName, longitude, latitude, location, timestamp)
}
