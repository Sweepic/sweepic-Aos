package com.umc.sweepic.domain.model.response.challenge

import com.umc.sweepic.domain.model.Challenge

data class CreateWeeklyChallengeResponseModel(
    val id : String,
    val title : String,
    val context : String,
    val requiredCount : Int,
    val remainingCount : Int,
    val userId : String,
    val createdAt : String,
    val updatedAt : String,
    val acceptedAt : String?,
    val completedAt : String?,
    val status : Int
)

