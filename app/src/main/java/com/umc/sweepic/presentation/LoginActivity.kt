package com.umc.sweepic.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.sweepic.databinding.ActivityLoginBinding
import com.umc.sweepic.presentation.onboarding.OnboardingStep1Activity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        // 카카오 로그인 버튼 클릭 시 Step1Activity로 이동
        binding.kakaoLoginLayout.setOnClickListener {
            navigateToStep1()
        }
        binding.naverLoginLayout.setOnClickListener {
            // 네이버 로그인 처리
        }
        binding.googleLoginLayout.setOnClickListener {
            // 구글 로그인 처리
        }
    }

    private fun navigateToStep1() {
        val intent = Intent(this, OnboardingStep1Activity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
