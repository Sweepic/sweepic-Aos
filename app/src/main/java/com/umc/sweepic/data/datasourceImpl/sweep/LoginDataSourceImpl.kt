package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.LoginDataSource
import com.umc.sweepic.data.dto.response.LoginResponseDto
import com.umc.sweepic.data.service.LoginService
import retrofit2.Response
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(
    private val loginService: LoginService
) : LoginDataSource {

    override suspend fun getKakaoLoginUrl(): Response<Void> {
        return loginService.getKakaoLoginUrl()
    }

    override suspend fun kakaoLoginCallback(): Response<LoginResponseDto> {
        return loginService.kakaoLoginCallback()
    }
}