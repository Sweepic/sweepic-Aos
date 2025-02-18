package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.GoalChallengeRequestDto

data class GoalChallengeRequestModel(
    val remaining: Int,
    val required: Int,
    val id: String
){
    fun toGoalChallengeRequestDto() =
        GoalChallengeRequestDto(remaining, required, id)
}
