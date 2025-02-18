package com.umc.sweepic.presentation.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.umc.sweepic.databinding.ActivityLoginBinding
import com.umc.sweepic.presentation.onboarding.OnboardingStep1Activity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        initView()
    }

    private fun initObservers() {
        loginViewModel.loginUrl.observe(this) { url ->
            Log.d("LoginActivity", "카카오 로그인 URL: $url")
            openKakaoLoginWithCustomTabs(url)
        }

        loginViewModel.sessionId.observe(this) { sessionId ->
            Log.d("LoginActivity", "세션 ID 저장 완료: $sessionId")
            saveSessionId(sessionId)
            navigateToStep1()
        }

        loginViewModel.error.observe(this) { errorMessage ->
            Log.e("LoginActivity", "오류 발생: $errorMessage")
        }
    }

    private fun initView() {
        binding.kakaoLoginLayout.setOnClickListener {
            loginViewModel.fetchKakaoLoginUrl()
        }
    }

    private fun openKakaoLoginWithCustomTabs(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun saveSessionId(sessionId: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("SESSION_ID", sessionId)
            apply()
        }
    }

    private fun navigateToStep1() {
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }
}
