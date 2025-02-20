package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.response.LoginResponseDto
import retrofit2.Response

interface LoginDataSource {
    suspend fun getKakaoLoginUrl(): Response<Void>
    suspend fun kakaoLoginCallback(): Response<LoginResponseDto>
}