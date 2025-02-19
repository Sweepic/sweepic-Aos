package com.umc.sweepic.domain.repository

import com.umc.sweepic.domain.model.response.login.LoginModel

interface LoginRepository {
    suspend fun getKakaoLoginUrl(): Result<String>
    suspend fun kakaoLoginCallback(): Result<LoginModel>

}