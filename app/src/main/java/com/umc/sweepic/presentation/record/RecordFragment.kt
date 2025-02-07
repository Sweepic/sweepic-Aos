package com.umc.sweepic.presentation.record

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentRecordBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.presentation.record.Adapter.ViewPagerAdapter

class RecordFragment : BaseFragment<FragmentRecordBinding>(R.layout.fragment_record) {

    override fun initObserver() {
        // 옵저버 초기화 (필요 시 추가)
    }

    override fun initView() {
        // ViewPager2와 Adapter 설정
        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.vpRecord.adapter = viewPagerAdapter

        // RecyclerView의 Nested Scrolling 설정 비활성화
        val recyclerView = binding.vpRecord.getChildAt(0) as RecyclerView
        recyclerView.isNestedScrollingEnabled = false

        // TabLayout과 ViewPager2 연동
        TabLayoutMediator(binding.tabRecord, binding.vpRecord) { tab, position ->
            tab.text = when (position) {
                0 -> "히스토리"  // 첫 번째 탭 이름
                1 -> "태그보드"  // 두 번째 탭 이름
                2 -> "메모"      // 세 번째 탭 이름
                else -> null
            }
        }.attach()

    }
}
