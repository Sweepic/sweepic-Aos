package com.umc.sweepic.presentation.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentNewChallengeBinding
import com.umc.sweepic.presentation.challenge.adapter.ChallengeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewChallengeFragment : Fragment() {
    private var _binding: FragmentNewChallengeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChallengeViewModel
    private lateinit var adapter: ChallengeAdapter  // ✅ lateinit 사용

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 초기화
        viewModel = ViewModelProvider(requireActivity()).get(ChallengeViewModel::class.java)

        // ✅ Adapter 초기화 전에 먼저 RecyclerView 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Adapter에 Challenge 수락 이벤트 추가
        adapter = ChallengeAdapter { challengeId ->
            viewModel.acceptChallenge(challengeId)
        }

        binding.recyclerView.adapter = adapter  // 🔥 여기서 초기화!

        // LiveData 옵저버 설정
        viewModel.newChallenges.observe(viewLifecycleOwner) { challenges ->
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

