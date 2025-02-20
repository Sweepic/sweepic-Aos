package com.umc.sweepic.domain.model.response.sweep

data class AiTagResponseModel(
    val labels: List<AiTagContentModel>
) {
    data class AiTagContentModel(
        val score: Double,
        val description: String
    )
}
