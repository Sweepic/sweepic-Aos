package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.response.LoginResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface LoginService {
    @GET("oauth2/login/kakao")
    suspend fun getKakaoLoginUrl(): Response<Void>

    @GET("oauth2/callback/kakao")
    suspend fun kakaoLoginCallback(): Response<LoginResponseDto>
}