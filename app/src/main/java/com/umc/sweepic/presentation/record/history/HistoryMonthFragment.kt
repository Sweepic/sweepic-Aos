package com.umc.sweepic.presentation.record.history

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthBinding
import com.umc.sweepic.presentation.base.BaseFragment

class HistoryMonthFragment : BaseFragment<FragmentHistoryMonthBinding>(R.layout.fragment_history_month) {

    private val viewModel: HistoryMonthViewModel by viewModels()

    override fun initObserver() {
        // ViewModel을 사용해 필요한 데이터 관찰 (LiveData 연동)
    }

    override fun initView() {
        // UI 초기화 및 이벤트 처리
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp() // 뒤로 가기 기능
        }

        binding.tvEdit.setOnClickListener {
            // 수정 버튼 클릭 시 로직 추가
        }
    }

    companion object {
        fun newInstance(): HistoryMonthFragment {
            return HistoryMonthFragment()
        }
    }
}
