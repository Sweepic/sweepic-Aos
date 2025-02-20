package com.umc.sweepic.presentation.challenge

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
    override fun initObserver() {
        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            if (userInfo != null) {
                goalUpdate(userInfo)
            }
        }
        viewModel.totalImageCount.observe(viewLifecycleOwner) { count ->
            binding.tvTotal.text = count.toString() // UI 업데이트
        }
    }

    override fun initView() {
        setUpMyPage()
        setupViewPagerAndTabs()
        viewModel.getUserInformation()
        viewModel.loadTotalImageCount()
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

}