package com.umc.sweepic.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.umc.sweepic.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 저장된 세션 확인
        val sessionId = getSessionId()
        Log.d("SplashActivity", "저장된 세션 ID: $sessionId")

        if (sessionId.isNullOrEmpty()) {
            // 세션이 없으면 바로 로그인 화면으로 이동
            navigateToLogin()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                checkSessionValidity(sessionId)
            }
        }
    }

    private fun getSessionId(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_ID", null)
    }

    private suspend fun checkSessionValidity(sessionId: String) {
        val isValid = validateSession(sessionId)

        withContext(Dispatchers.Main) {
            when (isValid) {
                "VALID" -> navigateToMain() // 세션이 유효하면 메인 화면으로 이동
                "INVALID" -> {
                    clearSession() // 세션 만료, 삭제하고 로그인 화면으로 이동
                    navigateToLogin()
                }
                else -> {
                    navigateToMain()
                }
            }
        }
    }

    private suspend fun validateSession(sessionId: String): String {
        return withContext(Dispatchers.IO) { // ✅ IO 스레드에서 실행
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://sweepic.store/user/mypage")
                    .addHeader("Cookie", "connect.sid=$sessionId")
                    .build()

                val response = client.newCall(request).execute()

                when (response.code) {
                    200 -> "VALID" // 세션 유효함
                    401 -> "INVALID" // 세션 관련 오류면 유효하지 않은걸로
                    else -> "ERROR" //
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
}
