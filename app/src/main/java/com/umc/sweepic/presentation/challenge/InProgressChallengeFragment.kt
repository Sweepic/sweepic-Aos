package com.umc.sweepic.presentation.challenge

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentChallengeListBinding
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressChallengeFragment: BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list){
    private lateinit var challengeAdapter: ChallengeAdapter
    private val viewModel: ChallengeViewModel by viewModels()

    override fun initObserver() {
        viewModel.inProgressChallengeList.observe(viewLifecycleOwner) { challenges ->
            challengeAdapter.submitList(challenges)
        }
    }

    override fun initView() {
        setupRecyclerView()
        viewModel.fetchGetChallenge()
    }

    private fun setupRecyclerView() {
        challengeAdapter = ChallengeAdapter(
            onAcceptChallenge = { challengeId ->
                viewModel.fetchAcceptChallenge(challengeId) // 챌린지 수락 API 호출
            }
        )
        binding.rvChallengeContainer.layoutManager = LinearLayoutManager(context)
        binding.rvChallengeContainer.adapter = challengeAdapter
    }
}
