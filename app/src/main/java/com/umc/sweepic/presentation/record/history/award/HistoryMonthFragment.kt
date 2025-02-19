package com.umc.sweepic.presentation.record.history.award

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthBinding
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryMonthFragment : BaseFragment<FragmentHistoryMonthBinding>(R.layout.fragment_history_month) {

    private val viewModel: HistoryMonthViewModel by viewModels()
    private lateinit var photoAdapter: ChoicePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initObserver()
        initView()

        // 🔥 JSON 파일에서 데이터 불러오기
        viewModel.loadLastMonthPhotos(requireContext())
    }

    override fun initObserver() {
        viewModel.bestPhotos.observe(viewLifecycleOwner) { photoPaths ->
            val selectedPhotos = photoPaths.map { path ->
                SelectedPhoto(mediaId = "", timestamp = "", photoPath = path)
            }
            photoAdapter.submitList(selectedPhotos)
        }
    }

    override fun initView() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvEdit.setOnClickListener {
            findNavController().navigate(R.id.action_historyMonthFragment_to_historyMonthChoiceFragment)
        }
    }

    /** 📌 RecyclerView 설정 */
    private fun setupRecyclerView() {
        photoAdapter = ChoicePhotoAdapter { /* 선택 이벤트 없음 */ }
        binding.rvMonthBestPhotos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = photoAdapter
        }
    }
}
