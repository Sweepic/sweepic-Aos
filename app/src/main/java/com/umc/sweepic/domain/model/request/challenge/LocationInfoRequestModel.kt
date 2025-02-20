package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.LocationInfoRequestDto

data class LocationInfoRequestModel(
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val id: String
){
    fun toLocationInfoRequestDto() =
        LocationInfoRequestDto(timestamp, latitude, longitude, displayName, id)
}
