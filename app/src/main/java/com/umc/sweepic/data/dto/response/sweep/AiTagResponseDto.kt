package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.AiTagResponseModel

data class AiTagResponseDto(
    val labels: List<AiTagContentDto>
){
    data class AiTagContentDto(
        val score: Double,
        val description: String
    ){
        fun toAiTagContentModel() =
            AiTagResponseModel.AiTagContentModel(score, description)
    }
    fun toAiTagResponseModel() =
        AiTagResponseModel(labels.map { it.toAiTagContentModel() })
}
