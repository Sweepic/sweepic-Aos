package com.umc.sweepic.presentation.mypage

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.sweepic.R
import com.umc.sweepic.presentation.MainActivity

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation Bar 숨기기
        (activity as? MainActivity)?.hideNavigationBar()

        // Back button
        view.findViewById<ImageView>(R.id.iv_back)?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Navigation Bar 다시 표시
        (activity as? MainActivity)?.showNavigationBar()
    }
}