package com.umc.sweepic.presentation.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.sweepic.databinding.ActivityOnboardingStep2Binding

class OnboardingStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""

        binding.tvOnbodingTitle.text = "$name"+"님,\n사진을 몇 장까지 정리하고 싶으세요?"

        // 다음 버튼 클릭
        binding.btnOnbodingNext.setOnClickListener {
            val targetNumber = binding.etOnbodingTargetnum.text.toString().trim()
            if (targetNumber.isEmpty()) {
                binding.etOnbodingTargetnum.error = "숫자를 입력해주세요."
            } else {
                // Step3Activity로 이동 (주석 처리된 부분 수정 가능)
                // val intent = Intent(this, Step3Activity::class.java)
                // intent.putExtra("photo_limit", targetNumber)
                // startActivity(intent)
                // finish()
            }
        }
    }
}
