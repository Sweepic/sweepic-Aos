package com.umc.sweepic.domain.model.request.sweep

import com.umc.sweepic.data.dto.request.sweep.CreateMemoFolderRequestDto

data class CreateMemoFolderRequestModel(
    val folderName: String
){
    fun toCreateMemoFolderRequestDto() =
        CreateMemoFolderRequestDto(folderName)
}
