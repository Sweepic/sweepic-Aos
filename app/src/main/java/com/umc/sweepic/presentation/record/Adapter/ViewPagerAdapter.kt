package com.umc.sweepic.presentation.record.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.sweepic.presentation.record.history.HistoryFragment
import com.umc.sweepic.presentation.record.memo.MemoFragment
import com.umc.sweepic.presentation.record.tagboard.TagBoardFragment

class ViewPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment()
            1-> TagBoardFragment()
            else -> MemoFragment()
        }

    }


}