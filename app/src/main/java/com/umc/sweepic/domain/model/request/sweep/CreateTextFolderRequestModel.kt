package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.CreateTextFolderRequestDto
import java.io.Serializable

data class CreateTextFolderRequestModel(
    val base64_image: String,
    val user_id: Long,
    val folder_name: String
): Serializable {
    fun toCreateTextFolderRequestDto() =
        CreateTextFolderRequestDto(
            base64_image, user_id, folder_name
        )
}