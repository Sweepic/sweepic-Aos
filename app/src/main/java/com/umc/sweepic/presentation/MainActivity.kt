package com.umc.sweepic.presentation

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityMainBinding
import com.umc.sweepic.presentation.sweep.SweepActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    override fun initView() {
        initNavigator()
        setupBottomNavigation()
    }

    override fun initObserver() {

    }

    private fun initNavigator() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.mainBnv.setupWithNavController(navController)
    }
    private fun setupBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sweepActivity -> {
                    // SweepActivity로 이동
                    startActivity(SweepActivity.newIntent(this, ""))
                    true
                }
                else -> {
                    // Navigation Graph로 Fragment 전환
                    navController.navigate(menuItem.itemId)
                    true
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        binding.mainBnv.selectedItemId = navController.currentDestination?.id ?: R.id.challengeFragment
    }
}