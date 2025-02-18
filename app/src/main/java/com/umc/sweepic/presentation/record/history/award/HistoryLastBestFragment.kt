package com.umc.sweepic.presentation.record.history.award

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.sweepic.databinding.FragmentHistoryLastBestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryLastBestFragment : Fragment() {

    private var _binding: FragmentHistoryLastBestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryLastBestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // TODO: API 연동 후 날짜와 사진 ID를 불러와서 UI 업데이트
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
