package com.umc.sweepic.presentation.record

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.sweepic.presentation.record.History.HistoryFragment
import com.umc.sweepic.presentation.record.Memo.MemoFragment
import com.umc.sweepic.presentation.record.TagBord.TagBoardFragment

class RecordVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment.newInstance()
            1 -> TagBoardFragment.newInstance()
            2 -> MemoFragment.newInstance()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}