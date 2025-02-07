package com.umc.sweepic.presentation.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.umc.sweepic.databinding.ActivityOnboardingStep4Binding
import com.umc.sweepic.presentation.MainActivity

class OnboardingStep4Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep4Binding
    private val REQUEST_NOTIFICATION_PERMISSION = 1002
    private var isPermissionRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingStep4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: ""


        binding.tvOnboardingTitle.text =
            "$name"+"님,\n실시간으로 사진이 대량 추가되거나\n새로운 챌린지가 생긴 경우\n알림을 보낼게요!"


        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission()
        }


        binding.btnOnboardingNext.setOnClickListener {
            goToMainActivity()
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isPermissionRequested) {
            isPermissionRequested = true // 🚀 권한 요청 플래그 설정
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java) // ✅ MainActivity로 이동
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // 🔹 기존 스택 삭제 (온보딩 종료)
        startActivity(intent)
        finish()
    }
}
