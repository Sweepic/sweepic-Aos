package com.umc.sweepic.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.umc.sweepic.R
import com.umc.sweepic.presentation.login.LoginActivity
import com.umc.sweepic.presentation.onboarding.OnboardingStep1Activity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 저장된 세션 확인
        val sessionId = getSessionId()
        Log.d("SplashActivity", "저장된 세션 ID: $sessionId")

        if (sessionId.isNullOrEmpty()) {
            // 세션이 없으면 바로 로그인 화면으로 이동
            navigateToLogin()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                checkSessionAndStatus(sessionId)
            }
        }
    }

    private fun getSessionId(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_ID", null)
    }

    private suspend fun checkSessionAndStatus(sessionId: String) {
        val userStatus = validateSessionAndFetchStatus(sessionId)

        withContext(Dispatchers.Main) {
            when (userStatus) {
                "VALID_EXISTING" -> navigateToMain()
                "VALID_NEW" -> navigateToOnboarding()
                "INVALID" -> {
                    clearSession()
                    navigateToLogin()
                }
                else -> navigateToMain() // 기본적으로 메인으로 이동 (예외 처리)
            }
        }
    }

    private suspend fun validateSessionAndFetchStatus(sessionId: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://sweepic.store/user/mypage")
                    .addHeader("Cookie", "connect.sid=$sessionId")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                when (response.code) {
                    200 -> {
                        val jsonObject = JSONObject(responseBody ?: "{}")
                        val status = jsonObject.optJSONObject("success")?.optInt("status", 1) ?: 1

                        Log.d("SplashActivity", "사용자 status: $status")
                        if (status == 2) {
                            "VALID_NEW"
                        } else {
                            "VALID_EXISTING"
                        }
                    }
                    401 -> "INVALID" // 세션 만료
                    else -> "ERROR"
                }
            } catch (e: IOException) {
                Log.e("SplashActivity", "네트워크 오류 발생: ${e.message}")
                "ERROR"
            }
        }
    }

    private fun clearSession() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().remove("SESSION_ID").apply()
        Log.d("SplashActivity", "세션 삭제 완료")
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboarding() {
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }
}
