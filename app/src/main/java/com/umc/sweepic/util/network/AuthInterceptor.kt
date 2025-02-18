package com.umc.sweepic.util.network

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionId = sharedPreferences.getString("SESSION_ID", null)

        val request = if (!sessionId.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Cookie", "SESSION_ID=$sessionId") // ✅ 세션 ID 자동 추가
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
