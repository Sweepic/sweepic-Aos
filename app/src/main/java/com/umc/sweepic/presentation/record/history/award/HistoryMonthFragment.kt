package com.umc.sweepic.presentation.record.history.award

import android.os.Bundle
import android.util.Log
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

        //viewModel.loadLastMonthPhotos(requireContext())
        viewModel.loadSelectedBestPhotos(requireContext())
    }

    override fun initObserver() {
       /* viewModel.bestPhotos.observe(viewLifecycleOwner) { photoPaths ->
            val selectedPhotos = photoPaths.map { path ->
                SelectedPhoto(mediaId = "", timestamp = "", photoPath = path)
            }
            photoAdapter.submitList(selectedPhotos)

        }*/
        viewModel.bestPhotos.observe(viewLifecycleOwner) { photoPaths ->
            Log.d("HistoryMonthFragment", "📸 업데이트된 사진 리스트: $photoPaths")

            if (photoPaths.isEmpty()) {
                Log.e("HistoryMonthFragment", "❌ 사진 리스트가 비어 있음! UI 업데이트 불가")
                return@observe
            }

            updateRecyclerView(photoPaths)
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
        photoAdapter = ChoicePhotoAdapter(
            onPhotoSelected = { /* 선택 이벤트 없음 */ },
            itemLayoutResId = R.layout.item_best_photo // ✅ `item_best_photo` 사용
        )
        binding.rvMonthBestPhotos.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = photoAdapter
            setHasFixedSize(true)
            itemAnimator = null
        }

        // ✅ 초기 빈 리스트 설정 (강제 리프레시 유도)
        photoAdapter.submitList(emptyList())
    }

    private fun updateRecyclerView(photoPaths: List<String>) {
        Log.d("HistoryMonthFragment", "📸 RecyclerView 업데이트 실행됨: $photoPaths")

        if (photoPaths.isEmpty()) {
            Log.e("HistoryMonthFragment", "❌ 사진 리스트가 비어 있음! UI 업데이트 불가")
            return
        }

        val selectedPhotos = photoPaths.map { path ->
            SelectedPhoto(mediaId = "", timestamp = "", photoPath = path)
        }

        binding.rvMonthBestPhotos.post {
            photoAdapter.submitList(emptyList()) // 🔥 기존 리스트 초기화 (강제 리프레시)
            photoAdapter.submitList(selectedPhotos.toMutableList()) // ✅ 새로운 리스트 적용
            photoAdapter.notifyDataSetChanged() // ✅ UI 강제 갱신
        }
    }

}
