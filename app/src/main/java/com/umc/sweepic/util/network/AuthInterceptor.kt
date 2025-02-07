package com.umc.sweepic.util.network

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val spf: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().addHeader("Cookie", "connect.sid= ${spf.getString("cookie", "")}").build()
        return chain.proceed(authRequest)
    }
}