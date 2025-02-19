package com.umc.sweepic.presentation.login

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityWebViewBinding
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("LOGIN_URL") ?: return
        setupWebView(url)
    }

    private fun setupWebView(url: String) {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(binding.webView, true)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebViewActivity", "페이지 로딩 완료: $url")

                if (url?.startsWith("https://sweepic.store") == true) {
                    extractSessionId(url)
                }
            }
        }

        binding.webView.loadUrl(url)
    }

    private fun extractSessionId(url: String) {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url) ?: ""

        cookies.split(";").forEach {
            val cookie = it.trim()
            if (cookie.startsWith("connect.sid=")) {
                val sessionId = cookie.split("=")[1]
                Log.d("WebViewActivity", "세션 ID 추출됨: $sessionId")

                val resultIntent = Intent().apply {
                    putExtra("SESSION_ID", sessionId)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun saveSessionId(sessionId: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("SESSION_ID", sessionId)
            commit()
        }
    }
}
