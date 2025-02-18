package com.umc.sweepic.util.network

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val spf: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().addHeader("Cookie", "connect.sid= ${spf.getString("cookie", "s%3AmWcJLERD5ujyWKEu3bzz_bjDOI_2sZb7.WWElujFz6pcXyAmKH6n4IJ4aGViZLmMgOkaAW6gj7%2FY")}").build()
        return chain.proceed(authRequest)
    }
}