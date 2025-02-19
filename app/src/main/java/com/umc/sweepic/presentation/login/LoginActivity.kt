package com.umc.sweepic.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            //세션이 있으면 로그인 건너뛰고 메인 화면으로 이동
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
            loginViewModel.fetchKakaoLoginUrl()
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
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
