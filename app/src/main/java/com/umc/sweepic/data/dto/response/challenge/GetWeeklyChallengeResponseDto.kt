package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetWeeklyChallengeResponseModel

data class GetWeeklyChallengeResponseDto (
    val id : String,
    val userId: String,
    val title : String,
    val context : String,
    val challengeDate: String?,
    val requiredCount : Int,
    val remainingCount : Int,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String?,
    val completedAt : String?,
    val status : Int
){
    fun toGetWeeklyChallengeResponseModel() =
        GetWeeklyChallengeResponseModel(
            id, userId, title, context, challengeDate, requiredCount, remainingCount, createdAt, updatedAt, acceptedAt, completedAt, status
        )
}