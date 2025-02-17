package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.LocationLogicResponseModel

data class LocationLogicResponseDto(
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val location: String,
    val timestamp: String
) {
    fun toCreateLocationLogicTestResponseModel() =
        LocationLogicResponseModel(
            id, displayName, latitude, longitude, location, timestamp
        )

    companion object {
        fun List<LocationLogicResponseDto>.toResponseModelList(): List<LocationLogicResponseModel> {
            return this.map { it.toCreateLocationLogicTestResponseModel() }
        }
    }
}
