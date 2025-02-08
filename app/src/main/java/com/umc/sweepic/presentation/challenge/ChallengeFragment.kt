package com.umc.sweepic.presentation.challenge

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.databinding.FragmentChallengeBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter
import com.umc.sweepic.presentation.challenge.adapter.ChallengePagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeFragment : BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {

    private val viewModel: ChallengeViewModel by viewModels()

    override fun initObserver() {
        viewModel.locationTestResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "API 응답 성공: $response")
                Toast.makeText(requireContext(), "API 응답 성공!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ChallengeFragment", "API 응답 실패")
                Toast.makeText(requireContext(), "API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.locationChallengeRespone.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "locationChallnege API 응답 성공: $response")
                Toast.makeText(requireContext(), "locationChallnege API 응답 성공!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ChallengeFragment", "locationChallnege API 응답 실패")
                Toast.makeText(requireContext(), "locationChallnege API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initView() {

        // iv_mypage 클릭 이벤트
        binding.ivMypage.setOnClickListener {
            findNavController().navigate(R.id.action_challengeFragment_to_myPageFragment)
        }

        setupViewPagerAndTabs()
        fetchChallengeTestData()
        fetchLocationChallengeData()
    }

    private fun fetchChallengeTestData() {
        val request = CreateLocationLogicTestRequestModel(
            id = "12",
            displayName = "string",
            latitude = 0.0,
            longitude = 0.0,
            timestamp = "string"
        )

        Log.d("ChallengeFragment", "API 요청 시작: $request")
        viewModel.fetchChallengeLocationLogicTestChallengeCreate(request)

    }

    private fun fetchLocationChallengeData(){
        val request = CreateLocationChallengeRequestModel(
            userId = "12",
            title = "string",
            context = "string",
            location = "string",
            required = 0
        )

        Log.d("ChallengeFragment", "locationChallnege API 요청 시작: $request")
        viewModel.fetchChallengeLocationChallengeCreate(request)
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