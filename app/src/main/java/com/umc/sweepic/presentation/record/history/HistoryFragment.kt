package com.umc.sweepic.presentation.record.history


import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentHistoryBinding
import com.umc.sweepic.presentation.base.BaseFragment
import com.umc.sweepic.presentation.record.history.award.HistoryMonthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(R.layout.fragment_history) {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()

        fetchLastMonthPhotos(requireContext())
        loadPhotosFromJson(requireContext())
        viewModel.loadSelectedBestPhotos(requireContext())
    }


    override fun initObserver() {
        viewModel.bestPhotos.observe(viewLifecycleOwner) { photos ->
            if (photos.isNotEmpty()) {
                displayPhotos(photos)
            }
        }
    }

    override fun initView() {
        // UI 초기화 추가
        binding.ivNextMonth.setOnClickListener {
            navigateToHistoryMonthFragment()
        }

        binding.ivNextLastBest.setOnClickListener {
            navigateToHistoryLastBestFragment()
        }

        //viewModel.imageIdCheck("2025-02-19T04:07:08.911Z", "image_123")

        //viewModel.getAwards()

        //loadSelectedBestPhotos(requireContext())

        //fetchLastMonthPhotos(requireContext())

        //loadPhotosFromJson(requireContext())
        viewModel.loadSelectedBestPhotos(requireContext())

    }
    private fun loadSelectedBestPhotos(context: Context) {
        try {
            val file = File(context.filesDir, "selected_best_photos.json")
            if (!file.exists()) {
                Log.e("HistoryFragment", "❌ 선택된 사진 JSON이 존재하지 않음")
                return
            }

            val json = FileReader(file).use { reader ->
                reader.readText()
            }

            val type = object : TypeToken<List<String>>() {}.type
            val photos: List<String> = Gson().fromJson(json, type)

            if (photos.isNotEmpty()) {
                displayPhotos(photos)
            }
        } catch (e: Exception) {
            Log.e("HistoryFragment", "❌ 선택된 사진 JSON 로드 실패: ${e.message}")
        }
    }

    private fun navigateToHistoryMonthFragment() {
        findNavController().navigate(R.id.historyMonthFragment)
    }

    private fun navigateToHistoryLastBestFragment(){
        findNavController().navigate(R.id.action_historyFragment_to_historyLastBestFragment)
    }

    private fun fetchLastMonthPhotos(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val photos = getLastMonthPhotos(context)
            savePhotosToJson(context, photos)
        }
    }

    private fun getLastMonthPhotos(context: Context): List<String> {
        val lastMonthPhotos = mutableListOf<String>()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)  // 저번달로 설정
        val lastMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val mediaId = it.getLong(idColumn)
                val filePath = it.getString(dataColumn)
                val timestampMillis = it.getLong(dateColumn)
                val photoDate = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date(timestampMillis))

                if (photoDate == lastMonth) {
                    lastMonthPhotos.add(filePath)
                }
            }
        }

        Log.d("HistoryFragment", "📸 저번달($lastMonth) 사진 개수: ${lastMonthPhotos.size}")
        return lastMonthPhotos
    }

    private fun savePhotosToJson(context: Context, photos: List<String>) {
        try {
            val file = File(context.filesDir, "last_month_photos.json")
            val json = Gson().toJson(photos)

            FileWriter(file).use { writer ->
                writer.write(json)
            }
            Log.d("HistoryFragment", "✅ 사진 JSON 저장 완료")
        } catch (e: Exception) {
            Log.e("HistoryFragment", "❌ JSON 저장 실패: ${e.message}")
        }
    }

    private fun loadPhotosFromJson(context: Context) {
        try {
            val file = File(context.filesDir, "last_month_photos.json")

            if (!file.exists()) {
                Log.e("HistoryFragment", "❌ JSON 파일이 존재하지 않음! 갤러리에서 불러옵니다.")
                val photos = getLastMonthPhotos(context) // ✅ 파일이 없으면 갤러리에서 다시 가져옴
                savePhotosToJson(context, photos) // ✅ JSON 파일 다시 저장
                displayPhotos(photos) // ✅ UI 업데이트
                return
            }

            val json = FileReader(file).use { reader -> reader.readText() }
            val type = object : TypeToken<List<String>>() {}.type
            val photos: List<String> = Gson().fromJson(json, type)

            if (photos.isNotEmpty()) {
                displayPhotos(photos)
            } else {
                Log.e("HistoryFragment", "❌ JSON 파일은 있지만 사진 리스트가 비어 있음!")
            }
        } catch (e: Exception) {
            Log.e("HistoryFragment", "❌ JSON 로드 실패: ${e.message}")
        }
    }


    private fun displayPhotos(photos: List<String>) {
        binding.apply {
            if (photos.isNotEmpty()) {
                Glide.with(requireContext()).load(photos[0]).into(ivMonthBestPhoto1)
            }
            if (photos.size > 1) {
                Glide.with(requireContext()).load(photos[1]).into(ivMonthBestPhoto2)
            }
            if (photos.size > 2) {
                Glide.with(requireContext()).load(photos[2]).into(ivMonthBestPhoto3)
            }
        }
    }


    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }
}
