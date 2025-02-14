package com.umc.sweepic.presentation.record.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.presentation.record.memo.HistoryBestPicAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {

    private val viewModel: HistoryTagViewModel by viewModels()

    private lateinit var bestPictureAdapter: HistoryBestPicAdapter
    private lateinit var pastBestPictureAdapter: HistoryBestPicAdapter

    override fun initObserver() {
        viewModel.mostTaggedData.observe(viewLifecycleOwner) { mostTagged ->
            binding.tvRecordVisittag.text = "#${mostTagged.success.find { it.tagCategoryId == "1" }?.content ?: "알 수 없음"}"
            binding.tvRecordPeopletag.text = "#${mostTagged.success.find { it.tagCategoryId == "2" }?.content ?: "알 수 없음"}"
            binding.tvRecordFoodtag.text = "#${mostTagged.success.find { it.tagCategoryId == "3" }?.content ?: "알 수 없음"}"
        }
    }

    override fun initView() {
        setupRecyclerViews()
        loadBestPictures()
        viewModel.fetchMostTaggedData()

        binding.icNext.setOnClickListener {
            findNavController().navigate(R.id.historyTagFragment)
        }
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    private fun setupRecyclerViews() {
        bestPictureAdapter = HistoryBestPicAdapter(emptyList())
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
