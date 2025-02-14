package com.umc.sweepic.presentation.onboarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.umc.sweepic.databinding.ActivityOnboardingStep2Binding

class OnboardingStep2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingStep2Binding
    private var name: String = ""
    private var isPermissionDenied = false
    private var isPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingStep2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("name") ?: ""
        binding.tvOnboardingTitle.text =
            "$name"+"님,\nSweepic은 사진 정리를 위해\n사진 접근 권한을 요청합니다.\n'모두 허용'을 선택하시면\n원활한 서비스 이용이 가능합니다."

        checkMediaPermission()

        binding.btnOnboardingNext.setOnClickListener {
            if (isPermissionGranted) {
                goToStep3()
            } else {
                requestMediaPermission()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkMediaPermission()
    }

    private fun checkMediaPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        isPermissionGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (isPermissionGranted) {
            isPermissionDenied = false
        }
    }
    private fun requestMediaPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION_CODE)
            }
            isPermissionDenied -> {
                Toast.makeText(this, "설정에서 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = android.net.Uri.parse("package:$packageName")
                }
                startActivity(intent)
            }

            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                goToStep3()
            } else {
                isPermissionDenied = true
            }
        }
    }

    private fun goToStep3() {
        val intent = Intent(this, OnboardingStep3Activity::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1001
    }
}
