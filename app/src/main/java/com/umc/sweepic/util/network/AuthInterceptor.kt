package com.umc.sweepic.util.network

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val spf: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().addHeader("Cookie", "connect.sid= ${spf.getString("cookie", "connect.sid=s%3ACni--i6VN30lnlMSCvt46gOEyx6NMl7H.nO6yvfpDYjPd7vJaKgxuQ2dAhhcJWDL2xr6rwWMQtZ8")})}").build()
        return chain.proceed(authRequest)
    }
}