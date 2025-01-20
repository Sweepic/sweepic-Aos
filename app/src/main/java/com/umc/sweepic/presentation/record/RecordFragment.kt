package com.umc.sweepic.presentation.record

import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentRecordBinding
import com.umc.sweepic.presentation.base.BaseFragment

class RecordFragment: BaseFragment<FragmentRecordBinding>(R.layout.fragment_record) {
    override fun initObserver() {

    }

    override fun initView() {
        // ViewPager2에 어댑터 연결
        val adapter = RecordVpAdapter(this)
        binding.vpRecord.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.tablayoutRecord, binding.vpRecord) { tab, position ->
            tab.text = when (position) {
                0 -> "히스토리"
                1 -> "태그보드"
                2 -> "메모"
                else -> null
            }
        }.attach()
    }
}