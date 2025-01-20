package com.umc.sweepic.presentation.record


import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryBinding
import com.umc.sweepic.presentation.base.BaseFragment

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {
    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        // UI 초기화 추가
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}