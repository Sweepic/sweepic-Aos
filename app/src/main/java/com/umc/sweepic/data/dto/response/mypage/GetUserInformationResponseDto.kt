package com.umc.sweepic.data.dto.response.mypage

import com.google.gson.annotations.SerializedName
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel

data class GetUserInformationResponseDto(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("goalCount") val goalCount: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) {
    fun toGetUserInformationResponseModel(): GetUserInformationResponseModel {
        return GetUserInformationResponseModel(
            id = id,
            email = email,
            name = name,
            goalCount = goalCount,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
