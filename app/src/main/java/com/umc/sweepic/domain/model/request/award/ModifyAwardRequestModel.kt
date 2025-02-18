package com.umc.sweepic.domain.model.request.award

import com.umc.sweepic.data.dto.request.ModifyAwardRequestDto
import java.io.Serializable

data class ModifyAwardRequestModel(
    val imageId: String
): Serializable {
    fun toModifyAwardRequestDto(): ModifyAwardRequestDto {
        return ModifyAwardRequestDto(imageId = imageId)
    }
}

