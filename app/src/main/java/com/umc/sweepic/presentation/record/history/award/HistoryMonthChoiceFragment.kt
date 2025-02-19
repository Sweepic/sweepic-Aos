package com.umc.sweepic.presentation.record.history.award

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryMonthChoiceBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.presentation.record.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileWriter

@AndroidEntryPoint
class HistoryMonthChoiceFragment :
    BaseFragment<FragmentHistoryMonthChoiceBinding>(R.layout.fragment_history_month_choice) {

    private val viewModel: HistoryMonthChoiceViewModel by viewModels()
    private lateinit var photoAdapter: ChoicePhotoAdapter
    private var selectedPhotoAdapter: ChoicePhotoAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            // ✅ 선택한 사진을 JSON 파일에 저장
            saveSelectedPhotosToJson(requireContext(), selectedPhotos.map { it.photoPath })

            /// ✅ `HistoryMonthViewModel` 업데이트 추가
            val historyMonthViewModel: HistoryMonthViewModel by viewModels()
            historyMonthViewModel.updateBestPhotos(selectedPhotos.map { it.photoPath })


            // ✅ `HistoryViewModel`에도 업데이트 요청
            val historyViewModel: HistoryViewModel by viewModels()
            historyViewModel.loadSelectedBestPhotos(requireContext())

            viewModel.processAwardFlow(selectedPhotos) {
                Log.d("HistoryMonthChoiceFragment", "✅ 모든 API 요청 완료, 화면 이동")
                findNavController().navigateUp()
            }
        }
    }

    private fun saveSelectedPhotosToJson(context: Context, photos: List<String>) {
        try {
            val file = File(context.filesDir, "selected_best_photos.json")
            val json = Gson().toJson(photos)

            FileWriter(file).use { writer ->
                writer.write(json)
            }
            Log.d("HistoryMonthChoiceFragment", "✅ 선택한 사진 JSON 저장 완료")
        } catch (e: Exception) {
            Log.e("HistoryMonthChoiceFragment", "❌ JSON 저장 실패: ${e.message}")
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
