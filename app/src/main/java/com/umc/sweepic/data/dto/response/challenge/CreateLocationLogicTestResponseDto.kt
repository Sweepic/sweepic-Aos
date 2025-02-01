package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import java.time.Instant

data class CreateLocationLogicTestResponseDto(
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
    val location: String
) {
    fun toCreateLocationLogicTestResponseModel() =
        CreateLocationLogicTestResponseModel(
            id, displayName, latitude, longitude, timestamp, location
        )
}
