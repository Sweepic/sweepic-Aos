package com.umc.sweepic.util.network

import android.content.Context
import android.content.Intent
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import com.umc.sweepic.presentation.login.LoginActivity

class AuthInterceptor(private val context: Context) : Interceptor {

    private val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionId = sharedPreferences.getString("SESSION_ID", null)

        if (sessionId.isNullOrEmpty()) {
            Log.e("AuthInterceptor", "세션 ID 없음")
            return chain.proceed(chain.request())
        }

        val request = chain.request().newBuilder()
            .addHeader("Cookie", "connect.sid=$sessionId")
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) { // 401 Unauthorized 응답 처리
            Log.e("AuthInterceptor", "세션 만료, 로그아웃 처리")

            // 세션 삭제
            sharedPreferences.edit().remove("SESSION_ID").apply()

            // 로그인 화면으로 이동
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }

        return response
    }
}
