package com.umc.sweepic.data.dto.response.sweep

import com.umc.sweepic.domain.model.response.sweep.CreateMemoFolderResponseModel

data class CreateMemoFolderResponseDto(
    val id: Int,
    val folderName: String
){
    fun toCreateMemoFolderResponseModel() =
        CreateMemoFolderResponseModel(id, folderName)
}
