package com.umc.sweepic.data.dto

data class BaseResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T,
)