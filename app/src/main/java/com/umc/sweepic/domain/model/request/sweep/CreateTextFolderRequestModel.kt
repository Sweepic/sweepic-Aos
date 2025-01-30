package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.CreateTextFolderRequestDto
import java.io.Serializable

data class CreateTextFolderRequestModel(
    val base64Image: String,
    val userId: Long,
    val folderName: String
): Serializable {
    fun toCreateTextFolderRequestDto() =
        CreateTextFolderRequestDto(
            base64Image, userId, folderName
        )
}