package com.umc.sweepic.domain.model.request.challenge

import com.umc.sweepic.data.dto.request.challenge.WeekChallengeRequestDto

data class WeekChallengeRequestModel(
    val required: Int,
    val challengeDate: String,
    val context: String
){
    fun toWeekChallengeRequestDto() =
        WeekChallengeRequestDto(required, challengeDate, context)
}
