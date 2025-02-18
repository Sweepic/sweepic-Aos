package com.umc.sweepic.presentation.record.history.award

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthChoiceBinding
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryMonthChoiceFragment : BaseFragment<FragmentHistoryMonthChoiceBinding>(R.layout.fragment_history_month_choice) {

    private val viewModel: HistoryMonthChoiceViewModel by viewModels()
    private lateinit var photoAdapter: ChoicePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initObserver()
        initView()
    }

    override fun initObserver() {
        viewModel.photoList.observe(viewLifecycleOwner) { photos ->
            photoAdapter.submitList(photos)
        }
    }

    override fun initView() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 저번달(1월)의 사진 가져오기
        viewModel.loadLastMonthPhotos(requireContext())
    }

    /** 📌 RecyclerView 설정 */
    private fun setupRecyclerView() {
        photoAdapter = ChoicePhotoAdapter()
        binding.rvPhotoGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 5)
            adapter = photoAdapter
        }
    }

    companion object {
        fun newInstance(): HistoryMonthChoiceFragment {
            return HistoryMonthChoiceFragment()
        }
    }
}
