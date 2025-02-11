package com.umc.sweepic.data.dto.response.challenge

import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel

data class CreateWeeklyChallengeResponseDto(
    val id : String,
    val title : String,
    val context : String,
    val requiredCount : Int,
    val remainingCount : Int,
    val userId : String,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String,
    val completedAt : String,
    val status : Int
){
    fun toCreateWeeklyChallengeResponseModel() =
        CreateWeeklyChallengeResponseModel(
            id, title, context, requiredCount, remainingCount, userId, createdAt, updatedAt, acceptedAt, completedAt, status
        )
}
