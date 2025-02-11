package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.presentation.record.memo.HistoryBestPicAdapter

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {

    private lateinit var bestPictureAdapter:HistoryBestPicAdapter
    private lateinit var pastBestPictureAdapter: HistoryBestPicAdapter

    override fun initObserver() {
    }

    override fun initView() {
        setupRecyclerViews()
        loadBestPictures()
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    private fun setupRecyclerViews() {
        bestPictureAdapter =HistoryBestPicAdapter(emptyList())
        pastBestPictureAdapter = HistoryBestPicAdapter(emptyList())

        binding.rvBestPictures.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestPictureAdapter
        }

        binding.rvBestPicturesPast.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = pastBestPictureAdapter
        }
    }

    private fun loadBestPictures() {
        // 🔹 Drawable 이미지 예시 리스트 (R.drawable.img1, img2, img3)
        val sampleImages = listOf(
            R.drawable.img_memo_imagelist,
            R.drawable.img_memo_imagelist,
            R.drawable.img_memo_imagelist,
            R.drawable.img_memo_imagelist,
            R.drawable.img_memo_imagelist,
        )

        bestPictureAdapter.updateData(sampleImages)
        pastBestPictureAdapter.updateData(sampleImages)
    }
}
