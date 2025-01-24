package com.umc.sweepic.presentation.challenge

import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.databinding.FragmentChallengeBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.R

class ChallengeFragment: BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {

    private lateinit var pagerAdapter: ChallengePagerAdapter

    private val viewModel: ChallengeViewModel by lazy {
        ViewModelProvider(this).get(ChallengeViewModel::class.java)
    }

    private lateinit var adapter: ChallengeAdapter

    override fun initObserver() {
        // LiveData 초기 상태 확인 (필요시 추가 로직 가능)
        viewModel.inProgressChallenges.observe(viewLifecycleOwner) { inProgress ->
            // 데이터를 확인하거나 추가 처리 가능
            if (inProgress.isEmpty()) {
                // 초기 상태에서 도전 중인 챌린지가 비어 있음을 확인
                println("도전 중인 챌린지 초기화 완료")
            }
        }
    }

    override fun initView() {

        // iv_mypage 클릭 이벤트
        binding.ivMypage.setOnClickListener {
            findNavController().navigate(R.id.action_challengeFragment_to_myPageFragment)
        }

        setupViewPagerAndTabs()
    }

    private fun setupViewPagerAndTabs() {
        val pagerAdapter = ChallengePagerAdapter(this)
        binding.vpChallenge.adapter = pagerAdapter

        binding.vpChallenge.isNestedScrollingEnabled = false
        binding.vpChallenge.getChildAt(0).setOnTouchListener { _, _ -> true }

        TabLayoutMediator(binding.tabLayout, binding.vpChallenge) { tab, position ->
            tab.text = when (position) {
                0 -> "새로운 챌린지"
                1 -> "도전 중인 챌린지"
                else -> null
            }
        }.attach()

    }
}