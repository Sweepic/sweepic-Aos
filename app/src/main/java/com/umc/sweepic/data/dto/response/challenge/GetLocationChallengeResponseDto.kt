package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetLocationChallengeResponseModel

data class GetLocationChallengeResponseDto (
    val id : String,
    val userId: String,
    val title : String,
    val context : String,
    val requiredCount : Int,
    val remainingCount : Int,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String?,
    val completedAt : String?,
    val status : Int,
){
    fun toGetLocationChallengeResponseModel() =
        GetLocationChallengeResponseModel(
            id = id,
            userId = userId,
            title = title,
            context = context,
            requiredCount = requiredCount,
            remainingCount = remainingCount,
            createdAt = createdAt,
            updatedAt = updatedAt,
            acceptedAt = acceptedAt,
            completedAt = completedAt,
            status = status
        )
}