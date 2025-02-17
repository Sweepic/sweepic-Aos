package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.LocationLogicRequestDto
import java.io.Serializable

data class LocationLogicRequestModel (
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val id: String
): Serializable{
    fun toCreateLocationLogicTestRequestDto() =
        LocationLogicRequestDto(
            timestamp, latitude, longitude, displayName, id
        )

    companion object {
        fun List<LocationLogicRequestModel>.toDtoList(): List<LocationLogicRequestDto> {
            return this.map { it.toCreateLocationLogicTestRequestDto() }
        }
    }
}