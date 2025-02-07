package com.umc.sweepic.data.dto

data class BaseResponse<T>(
    val resultType: String,
    val error: String?,
    val success: T
)