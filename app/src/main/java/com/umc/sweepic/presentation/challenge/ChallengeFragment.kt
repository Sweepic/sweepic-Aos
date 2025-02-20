package com.umc.sweepic.presentation.challenge

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.databinding.FragmentChallengeBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel
import com.umc.sweepic.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeFragment: BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {
    private val viewModel: ChallengeViewModel by viewModels()

    // ActivityResult API를 이용한 권한 요청 런처
    private val requestMediaLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한 부여됨: 필요한 작업 수행 또는 로그 출력
            Log.d("ChallengeFragment", "ACCESS_MEDIA_LOCATION 권한 부여됨")
        } else {
            // 권한 거부됨: 사용자에게 안내
            Toast.makeText(requireContext(), "미디어 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun initObserver() {
        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let { // userInfo가 null이 아닐 때만 UI 업데이트
                goalUpdate(it)
                viewModel.updateRecentImageCount()
            }
        }

        viewModel.todayImageCount.observe(viewLifecycleOwner) { todayCount ->
            todayCount?.let {
                binding.tvToday.text = it.toString()
                viewModel.updateRecentImageCount()
            } // 오늘 추가된 사진 개수 업데이트
        }

        viewModel.recentImageCount.observe(viewLifecycleOwner) { recentCount ->
            recentCount.let { binding.tvRecentNumber.text = it.toString() }
        }

        viewModel.totalImageCount.observe(viewLifecycleOwner) { count ->
            count?.let {
                binding.tvTotal.text = it.toString() // count 값이 null이 아닐 때만 UI 업데이트
            }
        }
    }

    override fun initView() {
        setUpMyPage()
        setupViewPagerAndTabs()
        viewModel.getUserInformation()
        viewModel.loadImageCounts()
        checkMediaLocationPermission()  // 권한 체크 및 요청
    }

    private fun setUpMyPage() {
        binding.ivMypage.setOnSingleClickListener {
            findNavController().navigate(R.id.action_challengeFragment_to_myPageFragment)
        }
    }

    private fun setupViewPagerAndTabs() {
        val adapter = ChallengePagerAdapter(this)
        binding.vpChallenge.adapter = adapter

        // TabLayoutMediator를 사용하여 TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tabLayout, binding.vpChallenge) { tab, position ->
            tab.text = when (position) {
                0 -> "새로운 챌린지"  // 첫 번째 탭 텍스트
                1 -> "도전 중인 챌린지" // 두 번째 탭 텍스트
                else -> ""
            }
        }.attach()
    }

    private fun goalUpdate(getUserInformationResponseModel: GetUserInformationResponseModel) {
        binding.tvName.text = getUserInformationResponseModel.name + "님"
        binding.tvTargetNumber.text = getUserInformationResponseModel.goalCount.toString()
    }

    private fun checkMediaLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_MEDIA_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestMediaLocationPermissionLauncher.launch(Manifest.permission.ACCESS_MEDIA_LOCATION)
        }
    }
}