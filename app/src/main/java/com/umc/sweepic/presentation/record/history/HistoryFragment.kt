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
import java.util.Calendar

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {

    private val viewModel: HistoryTagViewModel by viewModels()

    private lateinit var bestPictureAdapter: HistoryBestPicAdapter
    private lateinit var pastBestPictureAdapter: HistoryBestPicAdapter

    private var currentYear: Double = 0.0 // 현재 연도 저장 변수.
    private var currentMonth: Double = 0.0 // 현재 월 저장 변수.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentDate()
        initView()

        viewModel.fetchMostTaggedData(currentYear, currentMonth)

    }

    override fun initObserver() {
        viewModel.mostTaggedData.observe(viewLifecycleOwner) { mostTagged ->
            val placeTag = mostTagged[currentMonth]?.find { it.tagCategoryId == "1" }
            val peopleTag = mostTagged[currentMonth]?.find { it.tagCategoryId == "2" }
            val foodTag = mostTagged[currentMonth]?.find { it.tagCategoryId == "3" }

            binding.tvRecordMonthtitle.text = "${currentMonth.toInt()}월"

            binding.tvRecordVisittag.text = "#${placeTag?.content ?: "알 수 없음"}"
            binding.tvRecordPeopletag.text = "#${peopleTag?.content ?: "알 수 없음"}"
            binding.tvRecordFoodtag.text = "#${foodTag?.content ?: "알 수 없음"}"
        }
    }

    override fun initView() {
        setupRecyclerViews()
        loadBestPictures()

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


    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        currentYear = calendar.get(Calendar.YEAR).toDouble()
        currentMonth = (calendar.get(Calendar.MONTH) + 1).toDouble()
    }
}
