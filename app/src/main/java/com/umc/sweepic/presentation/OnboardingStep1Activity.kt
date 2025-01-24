package com.umc.sweepic.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.sweepic.databinding.ActivityOnboardingStep1Binding

class OnboardingStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityOnboardingStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다음 버튼 클릭 리스너
        binding.btnOnbodingNext.setOnClickListener {
            val name = binding.etOnbodingName.text.toString().trim() // 이름 입력받기
            if (name.isEmpty()) {
                binding.etOnbodingName.error = "이름을 입력해주세요." // 입력값이 없으면 에러 메시지 표시
            } else {
                // Step2Activity로 이동
                val intent = Intent(this, OnboardingStep2Activity::class.java)
                intent.putExtra("name", name) // 입력한 이름 전달
                startActivity(intent)
                finish() // Step1Activity 종료
            }
        }
    }
}
