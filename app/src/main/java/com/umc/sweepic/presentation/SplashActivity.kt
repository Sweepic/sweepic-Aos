package com.umc.sweepic.presentation
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import androidx.appcompat.app.AppCompatActivity
import com.umc.sweepic.presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 저장된 세션 확인
        val sessionId = getSessionId()
        Log.d("SplashActivity", "저장된 세션 ID: $sessionId")

        if (sessionId != null && sessionId.isNotEmpty()) {
            // 세션이 있으면 메인 화면으로
            navigateToMain()
        } else {
            // 세션이 없으면 로그인 화면으로
            navigateToLogin()
        }
    }

    private fun getSessionId(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_ID", null)
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
