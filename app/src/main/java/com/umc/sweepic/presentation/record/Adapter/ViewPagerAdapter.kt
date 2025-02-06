package com.umc.sweepic.presentation.record.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.sweepic.presentation.record.TagBoardFragment
import com.umc.sweepic.presentation.record.history.HistoryFragment

import com.umc.sweepic.presentation.record.memo.MemoFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment.childFragmentManager, fragment.lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment()
            1 -> TagBoardFragment()
            2 -> MemoFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
