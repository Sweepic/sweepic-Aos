package com.umc.sweepic.presentation.record.history.award

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthChoiceBinding
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryMonthChoiceFragment :
    BaseFragment<FragmentHistoryMonthChoiceBinding>(R.layout.fragment_history_month_choice) {

    private val viewModel: HistoryMonthChoiceViewModel by viewModels()
    private lateinit var photoAdapter: ChoicePhotoAdapter
    private var selectedPhotoAdapter: ChoicePhotoAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HistoryMonthChoiceFragment", "✅ onViewCreated 실행됨")

        binding.tvComplete.post {
            Log.d("HistoryMonthChoiceFragment", "✅ tvComplete 뷰가 존재함")

            binding.tvComplete.setOnClickListener {
                Log.d("HistoryMonthChoiceFragment", "✅ 완료 버튼 클릭됨")

                val selectedPhotos = viewModel.selectedPhotos.value ?: emptyList()

                if (selectedPhotos.isEmpty()) {
                    Log.d("HistoryMonthChoiceFragment", "⚠️ 선택된 사진이 없습니다.")
                    return@setOnClickListener
                }

                viewModel.processAwardFlow(selectedPhotos) {
                    Log.d("HistoryMonthChoiceFragment", "✅ 모든 API 요청 완료, 화면 이동")
                    findNavController().navigateUp() // ✅ 모든 요청 완료 후 화면 이동
                }
            }
        }
        setupRecyclerView()
        setupSelectedRecyclerView()
        initObserver()
        initView()
    }

    override fun initObserver() {
        viewModel.photoList.observe(viewLifecycleOwner) { photos ->
            photoAdapter.submitList(photos)
        }

        viewModel.selectedPhotos.observe(viewLifecycleOwner) { selectedPhotos ->
            selectedPhotoAdapter?.submitList(selectedPhotos)
        }
    }

    override fun initView() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        // 저번달(1월)의 사진 가져오기
        viewModel.loadLastMonthPhotos(requireContext())

        binding.tvComplete.setOnClickListener {
            Log.d("HistoryMonthChoiceFragment", "✅ 완료 버튼 클릭됨")

            val selectedPhotos = viewModel.selectedPhotos.value ?: emptyList()

            if (selectedPhotos.isEmpty()) {
                Log.d("HistoryMonthChoiceFragment", "⚠️ 선택된 사진이 없습니다.")
                return@setOnClickListener
            }

            // ✅ 순차적으로 API 호출 실행
            viewModel.processAwardFlow(selectedPhotos) {
                Log.d("HistoryMonthChoiceFragment", "✅ 모든 API 요청 완료, 화면 이동")
                findNavController().navigateUp() // ✅ 모든 요청 완료 후 화면 이동
            }
        }
    }

    /** 선택된 사진 RecyclerView 설정 */
    private fun setupSelectedRecyclerView() {
        selectedPhotoAdapter = ChoicePhotoAdapter { photo  ->
            viewModel.togglePhotoSelection(photo )
        }
        binding.rvSelectedPhotos.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = selectedPhotoAdapter
        }
    }

    /** 전체 사진 RecyclerView 설정 */
    private fun setupRecyclerView() {
        photoAdapter = ChoicePhotoAdapter { photo  ->
            viewModel.togglePhotoSelection(photo )
        }
        binding.rvPhotoGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = photoAdapter
        }
    }

    companion object {
        fun newInstance(): HistoryMonthChoiceFragment {
            return HistoryMonthChoiceFragment()
        }
    }
}
