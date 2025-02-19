package com.umc.sweepic.util.network

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {

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

        return chain.proceed(request)
    }
}
