package com.umc.sweepic.presentation.record.history.award

import android.content.Context
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class HistoryMonthFragment : BaseFragment<FragmentHistoryMonthBinding>(R.layout.fragment_history_month) {

    private val viewModel: HistoryMonthViewModel by viewModels()
    private lateinit var photoAdapter: ChoicePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initObserver()
        initView()
        updateTitles()

        if (isSelectedPhotosJsonExists(requireContext())) {
            Log.d("HistoryMonthFragment", "✅ 기존 선택된 사진이 있음 → loadSelectedBestPhotos() 실행")
            viewModel.loadSelectedBestPhotos(requireContext())
        } else {
            Log.d("HistoryMonthFragment", "🚀 처음 실행 → loadLastMonthPhotos() 실행")
            viewModel.loadLastMonthPhotos(requireContext())
        }
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
            onPhotoSelected = {  },
            itemLayoutResId = R.layout.item_best_photo, // ✅ `item_best_photo` 사용
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

    private fun isSelectedPhotosJsonExists(context: Context): Boolean {
        val file = File(context.filesDir, "selected_best_photos.json")
        return file.exists()
    }

    private fun updateTitles() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1) // ✅ 저번 달 설정
        val lastMonth = SimpleDateFormat("M", Locale.getDefault()).format(calendar.time)

        val titleText = "${lastMonth}월 베스트 사진 Top5"
        binding.tvBestPhotosTitle.text = titleText // ✅ TextView 업데이트

        Log.d("HistoryMonthFragment", "✅ 업데이트된 타이틀: $titleText")
    }

}