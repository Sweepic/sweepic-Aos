package com.umc.sweepic.presentation.record.history

import androidx.navigation.fragment.findNavController
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthChoiceBinding
import com.umc.sweepic.presentation.base.BaseFragment

class HistoryMonthChoiceFragment : BaseFragment<FragmentHistoryMonthChoiceBinding>(R.layout.fragment_history_month_choice) {
    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        // UI 초기화 추가
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        fun newInstance(): HistoryMonthChoiceFragment {
            return HistoryMonthChoiceFragment()
        }
    }
}
