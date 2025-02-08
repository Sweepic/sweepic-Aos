package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import java.io.Serializable
import java.time.Instant

data class CreateLocationLogicTestRequestModel (
    val id: String,
    val displayName: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
): Serializable{
    fun toCreateLocationLogicTestRequestDto() =
        CreateLocationLogicTestRequestDto(
            id, displayName, latitude, longitude, timestamp
        )

    companion object {
        fun List<CreateLocationLogicTestRequestModel>.toDtoList(): List<CreateLocationLogicTestRequestDto> {
            return this.map { it.toCreateLocationLogicTestRequestDto() }
        }
    }
}