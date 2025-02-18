package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.LoginDataSource
import com.umc.sweepic.domain.model.response.login.LoginModel
import com.umc.sweepic.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginDataSource: LoginDataSource
) : LoginRepository {

    override suspend fun getKakaoLoginUrl(): Result<String> {
        return runCatching {
            val response = loginDataSource.getKakaoLoginUrl()

            if (!response.isSuccessful) {
                throw Exception(
                    "Failed to fetch Kakao login URL: ${
                        response.errorBody()?.string()
                    }"
                )
            }

            response.raw().request.url.toString()
        }
    }

    override suspend fun kakaoLoginCallback(): Result<LoginModel> {
        return runCatching {
            val response = loginDataSource.kakaoLoginCallback()

            if (!response.isSuccessful) {
                throw Exception("로그인 콜백 실패: ${response.errorBody()?.string()}")
            }

            val cookies = response.headers().values("Set-Cookie")
            var sessionId: String? = null

            for (cookie in cookies) {
                if (cookie.startsWith("connect.sid")) {
                    sessionId = cookie.split("=")[1].split(";")[0]
                    break
                }
            }

            if (sessionId.isNullOrEmpty()) {
                throw Exception("세션 ID 없음")
            }

            return@runCatching LoginModel(
                token = sessionId,
                userId = 0,
                email = "",
                name = ""
            )
        }
    }
}
