package com.umc.sweepic.presentation.challenge.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.sweepic.presentation.challenge.InProgressChallengeFragment
import com.umc.sweepic.presentation.challenge.NewChallengeFragment

class ChallengePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // 두 개의 탭

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewChallengeFragment() // 새로운 챌린지
            1 -> InProgressChallengeFragment() // 도전 중인 챌린지
            else -> throw IllegalStateException("Invalid position $position")
        }
    }

}