package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.LocationChallengeRequestDto

data class LocationChallengeRequestModel(
    val required: Int,
    val location: String,
    val context: String
){
    fun toLocationChallengeRequestDto() =
        LocationChallengeRequestDto(required, location, context)
}
