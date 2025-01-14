package com.umc.sweepic.presentation

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun initView() {
        initNavigator()
        observeDestinationChange()
    }

    override fun initObserver() {

    }

    private fun initNavigator() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
    }

    private fun observeDestinationChange() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 특정 Fragment ID에서 BottomNavigationView 숨기기
            if (destination.id == R.id.fragment_mypage) {
                binding.mainBnv.visibility = android.view.View.GONE
            } else {
                binding.mainBnv.visibility = android.view.View.VISIBLE
            }
        }
    }

    // Navigation Bar 숨기기
    fun hideNavigationBar() {
        findViewById<View>(R.id.main_bnv)?.visibility = View.GONE
    }

    // Navigation Bar 표시
    fun showNavigationBar() {
        findViewById<View>(R.id.main_bnv)?.visibility = View.VISIBLE
    }
}