package com.umc.sweepic.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
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
            if (url.isNotEmpty()) {
                openKakaoLoginWithWebView(url)
            }
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

    @SuppressLint("SetJavaScriptEnabled")
    private fun openKakaoLoginWithWebView(url: String) {
        // WebView를 보이게 함
        binding.webView.visibility = View.VISIBLE

        // WebView 설정
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 쿠키 매니저를 통해 쿠키 읽기
                val cookieManager = CookieManager.getInstance()
                val cookies = cookieManager.getCookie(url)
                Log.d("LoginActivity", "쿠키: $cookies")
                // 쿠키에서 "SESSION_ID" 값을 추출
                cookies?.split(";")?.map { it.trim() }?.find { it.startsWith("SESSION_ID=") }?.let { sessionCookie ->
                    val sessionId = sessionCookie.substringAfter("=")
                    if (sessionId.isNotEmpty()) {
                        Log.d("LoginActivity", "세션 ID: $sessionId")
                        // 세션 ID 저장 후 다음 화면으로 이동
                        saveSessionId(sessionId)
                        navigateToStep1()
                    }
                }
            }
        }
        binding.webView.loadUrl(url)
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
