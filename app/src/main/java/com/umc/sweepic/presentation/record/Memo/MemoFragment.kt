package com.umc.sweepic.presentation.record.Memo

import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentMemoBinding
import com.umc.sweepic.presentation.base.BaseFragment

class MemoFragment : BaseFragment<FragmentMemoBinding>(R.layout.fragment_memo) {
    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        // UI 초기화 추가
    }

    companion object {
        fun newInstance(): MemoFragment {
            return MemoFragment()
        }
    }
}
