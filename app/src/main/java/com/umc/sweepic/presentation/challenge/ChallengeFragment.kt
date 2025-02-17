package com.umc.sweepic.presentation.challenge

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.databinding.FragmentChallengeBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.R
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter
import com.umc.sweepic.presentation.challenge.adapter.ChallengePagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChallengeFragment : BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {

    private val viewModel: ChallengeViewModel by viewModels()
    private lateinit var newAdapter: ChallengeAdapter
    private lateinit var inProgressAdapter: ChallengeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newAdapter = ChallengeAdapter { challengeId ->
            viewModel.acceptChallenge(challengeId)
        }

        inProgressAdapter = ChallengeAdapter { challengeId ->
            Log.d("ChallengeFragment", "🔄 진행 중인 챌린지는 수락 불가능: $challengeId")
        }

        initView()
        initObserver()

/*        // 📌 기기 내 사진 데이터를 서버로 전송
        lifecycleScope.launch {
            viewModel.fetchAndProcessLocationPhotos(requireContext())
        }*/

        viewModel.loadChallenges()
        viewModel.createWeeklyChallenge(requireContext())
        //viewModel.fetchAndProcessLocationPhotos(requireContext())
    }

    override fun initObserver() {
        viewModel.newChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("ChallengeFragment", "🆕 새로운 챌린지 개수: ${challenges.size}")
        }

        viewModel.inProgressChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("ChallengeFragment", "🔄 진행 중인 챌린지 개수: ${challenges.size}")
        }
    }

    override fun initView() {
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
