package com.umc.sweepic.presentation.challenge

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.databinding.FragmentChallengeBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.Challenge
import com.umc.sweepic.domain.model.request.challenge.CreateChallengeUpdateRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter
import com.umc.sweepic.presentation.challenge.adapter.ChallengePagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChallengeFragment : BaseFragment<FragmentChallengeBinding>(R.layout.fragment_challenge) {

    private val viewModel: ChallengeViewModel by viewModels()
    private lateinit var newChallengeAdapter: ChallengeAdapter
    private lateinit var inProgressChallengeAdapter: ChallengeAdapter
    private lateinit var completedChallengeAdapter: ChallengeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sendDevicePhotosToServer(requireContext())
        viewModel.fetchLocationBasedChallenge(requireContext())
        viewModel.fetchDevicePhotosAndCreateWeeklyChallenges(requireContext())
    }


    override fun initObserver() {
        viewModel.updateResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "update API 응답 성공: $response")
                Toast.makeText(requireContext(), "update API 응답 성공!", Toast.LENGTH_SHORT).show()

                binding.tvRecentNumber.text = response.remaining.toString()
                binding.tvTotal.text = response.required.toString()

            } else {
                Log.e("ChallengeFragment", "update API 응답 실패")
                Toast.makeText(requireContext(), "update API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getChallengeResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "getChallenge API 응답 성공: $response")
                Toast.makeText(requireContext(), "getChallenge API 응답 성공!", Toast.LENGTH_SHORT).show()


            } else {
                Log.e("ChallengeFragment", "getChallenge API 응답 실패")
                Toast.makeText(requireContext(), "getChallenge API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.locationTestResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "location logic test API 응답 성공: $response")
                Toast.makeText(requireContext(), "location logic test API 응답 성공!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ChallengeFragment", "location logic test API 응답 실패")
                Toast.makeText(requireContext(), "location logic test API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }
        /*viewModel.locationChallengeResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "locationChallnege API 응답 성공: $response")
                Toast.makeText(requireContext(), "locationChallnege API 응답 성공!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ChallengeFragment", "locationChallnege API 응답 실패")
                Toast.makeText(requireContext(), "locationChallnege API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }*/
        viewModel.weeklyChallengeResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ChallengeFragment", "weekly API 응답 성공: $response")
                Toast.makeText(requireContext(), "weekly API 응답 성공!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("ChallengeFragment", "weekly API 응답 실패")
                Toast.makeText(requireContext(), "weekly API 응답 실패!", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.locationTestResponse.observe(viewLifecycleOwner) { filteredPhotos ->
            if (filteredPhotos.isNotEmpty()) {
                Log.d("ChallengeFragment", "필터링된 사진 리스트: $filteredPhotos")
                Toast.makeText(requireContext(), "필터링된 사진이 확인되었습니다!", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ChallengeFragment", "필터링된 사진 없음")
            }
        }
        viewModel.locationChallengeResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("ChallengeFragment", "위치 기반 챌린지 생성 완료: $response")
                Toast.makeText(requireContext(), "위치 기반 챌린지가 생성되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun initView() {


        // iv_mypage 클릭 이벤트
        binding.ivMypage.setOnClickListener {
            findNavController().navigate(R.id.action_challengeFragment_to_myPageFragment)
        }

        setupViewPagerAndTabs()
        fetchChallengeUpdateData()
        fetchChallengeGet()
        //fetchChallengeTestData()
        //fetchLocationChallengeData()
        //fetchLocationChallenges()
        //fetchWeeklyChallengeCreate()
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {


    }

    private fun fetchChallengeUpdateData(){
        val request = CreateChallengeUpdateRequestModel(
            id = "45",
            required = 0,
            remaining = 0
        )

        Log.d("ChallengeFragment", "update API 요청 시작: $request")
        viewModel.fetchChallengeUpdateCreate(request)
    }

    private fun fetchChallengeGet(){

        Log.d("ChallengeFragment", "getChallenge API 요청 시작: ")
        viewModel.fetchChallengeGet()
    }

    /*private fun fetchLocationChallenges() {
        viewModel.fetchLocationBasedPhotos()
    }
*/
  /*  private fun fetchChallengeTestData() {
        val request = CreateLocationLogicTestRequestModel(
            id = "12",
            displayName = "string",
            latitude = 0.0,
            longitude = 0.0,
            timestamp = "string"
        )

        Log.d("ChallengeFragment", "location logic test API 요청 시작: $request")
        viewModel.fetchChallge(request)

    }

    private fun fetchLocationChallengeData(){
        val request = CreateLocationChallengeRequestModel(
            context = "string",
            location = "string",
            required = 0
        )

        Log.d("ChallengeFragment", "locationChallnege API 요청 시작: $request")
        viewModel.fetchChallengeLocationChallengeCreate(request)
    }*/

    /*private fun fetchWeeklyChallengeCreate() {
        val request = CreateWeeklyChallengeRequestModel(
            context = "string",
            challengeDate = "string",
            required = 0
        )

        Log.d("ChallengeFragment", "Weekly challenge API 요청 시작: $request")
        viewModel.fetchWeeklyChallengeCreate(request)

    }*/



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