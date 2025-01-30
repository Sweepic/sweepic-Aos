package com.umc.sweepic.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityOnboardingStep3Binding

class OnboardingStep3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep3Binding
    private var imageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingStep3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""
        binding.tvOnboardingTitle.text = "$name 님,\n사진을 몇 장까지 정리하고 싶으세요?"

        imageCount = getImageCount(this)

        setupEditTextListener()

        binding.btnOnboardingNext.setOnClickListener {
            validateInput()
        }
    }

    private fun getImageCount(context: Context): Int {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )
        val count = cursor?.count ?: 0
        cursor?.close()
        return count
    }

    private fun setupEditTextListener() {
        binding.etOnboardingTargetnum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString().trim()

                if (inputText.isEmpty()) {
                    resetErrorUI()
                    return
                }

                if (!inputText.matches(Regex("\\d+"))) {
                    showError("숫자만 입력할 수 있습니다.")
                } else {
                    resetErrorUI()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateInput() {
        val inputText = binding.etOnboardingTargetnum.text.toString().trim()

        if (inputText.isEmpty()) {
            showError("숫자를 입력해주세요.")
            return
        }

        if (!inputText.matches(Regex("\\d+"))) {
            showError("숫자만 입력할 수 있습니다.")
            return
        }

        try {
            val inputNumber = inputText.toInt()
            when {
                inputNumber > imageCount -> {
                    showError("현재 사진 수보다 많은 목표는 설정할 수 없습니다.")
                }

                else -> {
                    goToNextStep(inputNumber)
                }
            }
        } catch (e: NumberFormatException) {
            showError("숫자만 입력할 수 있습니다.")
        }
    }

    private fun showError(message: String) {
        binding.tvNumberError.text = message
        binding.tvNumberError.visibility = View.VISIBLE
        binding.etOnboardingTargetnum.setBackgroundTintList(
            ContextCompat.getColorStateList(this, R.color.error)
        )
        binding.icError.visibility = View.VISIBLE
        binding.etOnboardingTargetnum.setTextColor(ContextCompat.getColor(this, R.color.error))
    }

    private fun resetErrorUI() {
        binding.tvNumberError.visibility = View.INVISIBLE
        binding.etOnboardingTargetnum.setBackgroundTintList(
            ContextCompat.getColorStateList(this, R.color.sw_gray2)
        )
        binding.icError.visibility = View.GONE
        binding.etOnboardingTargetnum.setTextColor(ContextCompat.getColor(this, R.color.black))
    }

    private fun goToNextStep(targetNumber: Int) {
        val name = intent.getStringExtra("name") ?: ""
        val intent = Intent(this, OnboardingStep4Activity::class.java)
        intent.putExtra("photo_limit", targetNumber)
        intent.putExtra("name", name)
        startActivity(intent)
        finish()
    }
}
