package com.umc.sweepic.domain.model

import com.umc.sweepic.domain.model.response.challenge.GetChallengeResponseModel
import com.umc.sweepic.domain.model.sweep.Gallery

data class ChallengeWithImages(
    val challenge: GetChallengeResponseModel,
    val images: List<Gallery>
)
