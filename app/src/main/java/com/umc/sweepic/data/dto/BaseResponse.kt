package com.umc.sweepic.data.dto

data class BaseResponse<T>(
    val resultType: String, // 예: SUCCESS, FAILURE
    val error: String?, // 에러 메시지 또는 null
    val success: T // 성공 데이터, 제네릭으로 처리
)