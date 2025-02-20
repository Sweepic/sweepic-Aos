package com.umc.sweepic.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.umc.sweepic.databinding.ActivityLoginBinding
import com.umc.sweepic.presentation.MainActivity
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

        val storedSessionId = getSessionId()
        Log.d("LoginActivity", "현재 저장된 세션 ID: $storedSessionId")

        if (storedSessionId != null) {
            // 세션이 있으면 로그인 건너뛰고 메인 화면으로 이동
            navigateToMain()
        }

        initObservers()
        initView()
    }

    private fun initObservers() {
        loginViewModel.sessionId.observe(this) { sessionId ->
            Log.d("LoginActivity", "세션 ID 저장 완료: $sessionId")
            saveSessionId(sessionId)
            navigateToStep1()
        }
    }

    private fun initView() {
        binding.kakaoLoginLayout.setOnClickListener {
            Log.d("LoginActivity", "카카오 로그인 버튼 클릭됨")
            loginViewModel.fetchKakaoLoginUrl()
        }

        loginViewModel.loginUrl.observe(this) { url ->
            Log.d("LoginActivity", "WebView 실행, URL: $url")

            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra("LOGIN_URL", url)
            }
            webViewLauncher.launch(intent)
        }
    }

    // WebViewActivity 결과 처리
    private val webViewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            val sessionId = result.data?.getStringExtra("SESSION_ID")
            Log.d("LoginActivity", "WebView에서 받아온 세션 아이디: $sessionId")

            if (!sessionId.isNullOrEmpty()) {
                saveSessionId(sessionId)
                navigateToStep1()
            } else {
                Log.e("LoginActivity", "세션 ID가 비어 있음")
            }
        } else {
        }
    }

    private fun saveSessionId(sessionId: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("SESSION_ID", sessionId)
            commit()
        }
    }

    private fun getSessionId(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("SESSION_ID", null)
    }

    private fun navigateToStep1() {
        Log.d("LoginActivity", "온보딩 화면으로 이동")
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        Log.d("LoginActivity", "메인 화면으로 이동")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
