package com.umc.sweepic.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityOnboardingStep1Binding

class OnboardingStep1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep1Binding
    private var isValidName = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingStep1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEditTextListener()
        setupNextButton()
    }

    private fun setupEditTextListener() {
        binding.etOnboardingName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLength = s?.length ?: 0

                binding.tvNameCount.text = "$currentLength/10"

                if (currentLength > 10) {
                    isValidName = false
                    binding.tvNameError.text = "10자를 초과할 수 없습니다."
                    binding.tvNameError.visibility = android.view.View.VISIBLE
                    binding.etOnboardingName.setTextColor(getColor(R.color.error))
                    binding.tvNameCount.setTextColor(getColor(R.color.error))
                    binding.etOnboardingName.setBackgroundTintList(
                        ContextCompat.getColorStateList(
                            getApplicationContext(),
                            R.color.error
                        )
                    )
                    binding.icError.visibility = android.view.View.VISIBLE

                } else {
                    isValidName = true
                    binding.tvNameError.visibility = android.view.View.INVISIBLE
                    binding.etOnboardingName.setTextColor(getColor(android.R.color.black))
                    binding.tvNameCount.setTextColor(getColor(android.R.color.darker_gray))
                    binding.etOnboardingName.setBackgroundTintList(
                        ContextCompat.getColorStateList(
                            getApplicationContext(),
                            R.color.sw_gray2
                        )
                    );
                    binding.icError.visibility = android.view.View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupNextButton() {
        binding.btnOnboardingNext.setOnClickListener {
            if (!isValidName) {
            } else {
                val name = binding.etOnboardingName.text.toString().trim()
                val intent = Intent(this, OnboardingStep2Activity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
                finish()
            }
        }
    }
    }