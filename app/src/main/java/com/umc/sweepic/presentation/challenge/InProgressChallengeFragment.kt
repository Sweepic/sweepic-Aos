package com.umc.sweepic.presentation.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.databinding.FragmentInProgressChallengeBinding
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressChallengeFragment : Fragment() {

    private var _binding: FragmentInProgressChallengeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var adapter: ChallengeAdapter  // ✅ Adapter 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInProgressChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity()).get(ChallengeViewModel::class.java)

        // ✅ RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Adapter에 Challenge 수락 이벤트 추가
        adapter = ChallengeAdapter { challengeId ->
            viewModel.acceptChallenge(challengeId)
        }

        binding.recyclerView.adapter = adapter

        // 📌 LiveData 옵저빙 - 진행 중인 챌린지
        viewModel.inProgressChallenges.observe(viewLifecycleOwner) { challenges ->
            Log.d("InProgressChallengeFragment", "📌 진행 중 챌린지 개수: ${challenges.size}")
            challenges.forEach {
                Log.d("InProgressChallengeFragment", "📌 챌린지 제목: ${it.title}, 날짜: ${it.challengeDate}")
            }
            adapter.setChallenges(challenges)
        }

        // 데이터 로드
        viewModel.loadChallenges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
