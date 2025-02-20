package com.umc.sweepic.presentation.record.tagboard

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentTagBoardBinding
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TagBoardFragment : BaseFragment<FragmentTagBoardBinding>(R.layout.fragment_tag_board) {

    private val viewModel: TagBoardViewModel by viewModels()

    private lateinit var rvAdapter: ImgGroupRVA
    private lateinit var monthAdapter: MonthAdapter
    private var isPopupVisible = false
    private val imagesByDate = mutableMapOf<String, MutableList<String>>()
    private val tagsByDate = mutableMapOf<String, List<String>>()

    private var selectedYear: String? = null
    private var selectedMonth: String? = null

    private val _imageGroups = MutableLiveData<Map<String, List<String>>>()  // ✅ 추가된 부분
    val imageGroups: LiveData<Map<String, List<String>>> get() = _imageGroups


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchFeature()
        observeViewModel()

        viewModel.fetchDateTagsFromMediaStore(requireContext())
    }

    private fun observeViewModel() {
        viewModel.imageDateTags.observe(viewLifecycleOwner) { tagsByDate ->
            viewModel.imageGroups.observe(viewLifecycleOwner) { imagesByDate ->
                if (tagsByDate != null && imagesByDate != null) {
                    updateImgGroupRecyclerView(tagsByDate, imagesByDate)
                }
            }
        }
    }

    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        setupYearSelector()
        setupMonthRecyclerView()
        checkPermissions()
    }

    private fun testGetImageTags(mediaId: Double) {
        viewModel.fetchImageTags(mediaId)  // ✅ ViewModel을 통해 API 호출

        viewModel.imageTags.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("tagboard", "이미지 태그 조회 성공: $response")
            } else {
                Log.e("tagboard", "이미지 태그 조회 실패: 응답이 null입니다.")
            }
        }
    }

    private fun testGetDateTags(year: Double, month: Double, date: Double) {
        viewModel.fetchDateTags(year, month, date)  // ✅ ViewModel을 통해 API 호출

        viewModel.imageTags.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("tagboard", "날짜 기반 태그 조회 성공: $response")
            } else {
                Log.e("tagboard", "날짜 기반 태그 조회 실패: 응답이 null입니다.")
            }
        }
    }



    // ✅ 날짜별 태그 & 이미지 그룹을 RecyclerView에 업데이트하는 함수
    private fun updateImgGroupRecyclerView(tagsByDate: Map<String, List<String>>, imagesByDate: Map<String, List<String>>) {
        binding.rcImgGroups.layoutManager = LinearLayoutManager(requireContext())
        binding.rcImgGroups.adapter = ImgGroupAdapter(tagsByDate, imagesByDate) { selectedTag ->
            binding.etSearch.setText(selectedTag)
            viewModel.searchImgGroupsByTag(selectedTag, imagesByDate, tagsByDate)
        }
    }

    private fun checkPermissions() {
        val requiredPermissions =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                // Android 13 이상
                listOf(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                // Android 13 미만
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        val deniedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isNotEmpty()) {
            requestPermissions(deniedPermissions.toTypedArray(), 1001)
        } else {
            loadImagesFromMediaStore() // 권한이 이미 허용된 경우
        }
    }


    private fun setupMonthRecyclerView() {
        val months =
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        monthAdapter = MonthAdapter(months) { selectedMonth ->
            // 월 선택 시 로직 처리
            Log.d("TagBoardFragment", "Selected month: $selectedMonth")
            onMonthSelected(selectedMonth)
        }

        binding.rcMonth.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = monthAdapter
        }

        // 연도 표시
        binding.tvYear.text = "2025" // 연도는 동적으로 변경 가능
    }


    private fun loadImagesFromMediaStore() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val query = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        val combinedList = mutableListOf<Triple<Long, String, String>>() // (timestamp, dateKey, imageUri)


        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                // 가능한 모든 날짜 필드 확인
                val dateTaken = cursor.getLong(dateTakenColumn)
                val dateModified = cursor.getLong(dateModifiedColumn) * 1000 // 초 단위 → 밀리초
                val dateAdded = cursor.getLong(dateAddedColumn) * 1000 // 초 단위 → 밀리초

                // 가장 최신 값을 사용
                val timestamp = maxOf(dateTaken, dateModified, dateAdded)

                // 날짜가 유효하지 않은 경우 건너뛰기
                if (timestamp == 0L) continue

                val fullDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
                val formattedDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date(timestamp))
                val year = fullDate.split("-")[0] // 연도 추출

                // 이미지 URI 생성
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                ).toString()

                // 날짜별 데이터 추가
                combinedList.add(Triple(timestamp, "$year-$formattedDate", imageUri))
            }
        }

        // 데이터 정렬: 내림차순
        val sortedList = combinedList.sortedByDescending { it.first }

        // 데이터를 그룹화하여 imagesByDate에 저장
        sortedList.forEach { (_, dateKey, imageUri) ->
            imagesByDate.getOrPut(dateKey) { mutableListOf() }.add(imageUri)
            tagsByDate[dateKey] = generateTagsForDate(dateKey)
        }

        // RecyclerView에 정렬된 데이터 적용
        if (imagesByDate.isNotEmpty()) {
            setupRecyclerView(imagesByDate, tagsByDate)
        } else {
            Log.d("TagBoardFragment", "No images found in MediaStore")
        }
    }



    private fun setupRecyclerView(
        imagesByDate: Map<String, List<String>>,
        tagsByDate: Map<String, List<String>>
    ) {
        val distinctImagesByDate = imagesByDate.mapValues { (_, images) ->
            images.distinct() // ✅ 중복 제거
        }

        rvAdapter = ImgGroupRVA(
            distinctImagesByDate,
            tagsByDate,
            { date, images, tags ->
                val bundle = Bundle().apply {
                    putString("date", date)
                    putStringArrayList("images", ArrayList(images))
                    putStringArrayList("tags", ArrayList(tags))
                }

                val parentNavController = requireActivity()
                    .supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment)
                    ?.let { it as? NavHostFragment }
                    ?.navController

                parentNavController?.navigate(R.id.action_recordFragment_to_detailImgFragment, bundle)
            },
            { selectedTag ->
                onTagClicked(selectedTag)
            }
        )

        // 태그보드 RecyclerView 설정
        binding.rcTagboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTagboard.adapter = rvAdapter
    }


    private fun generateTagsForDate(date: String): List<String> {
        return listOf("#잠실", "#지은", "#인하대학교") // 예제 태그
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            val allGranted = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allGranted) {
                loadImagesFromMediaStore()
            } else {
                Log.e("TagBoardFragment", "Permission denied by user")
                showPermissionDeniedDialog()
            }
        }
    }


    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한 요청")
            .setMessage("앱이 사진 및 동영상을 읽으려면 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun setupYearSelector() {
        binding.tvYear.setOnClickListener {
            showCustomYearPopup()
        }
    }


    private fun showCustomYearPopup() {
        val years = getAvailableYears() // 기기에서 연도 데이터 가져오기

        // 팝업 레이아웃 인플레이트
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_year, null)
        val yearContainer = popupView.findViewById<LinearLayout>(R.id.year_container)

        // 연도 데이터 동적 추가
        years.forEach { year ->
            val yearTextView = TextView(requireContext()).apply {
                text = year
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
                setPadding(20, 10, 20, 10)
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                //setBackgroundResource(R.drawable.bg_year_item)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    updateYear(year) // 선택된 연도 처리
                }
            }
            yearContainer.addView(yearTextView)
        }

        // PopupWindow 생성 및 크기 설정
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true)

        // 배경 클릭 시 닫기 설정
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // tvYear의 위치를 가져와 팝업을 바로 위에 표시
        val location = IntArray(2)
        binding.tvYear.getLocationOnScreen(location) // tvYear의 화면 좌표 가져오기
        val x = location[0]
        val y = location[1]

        // PopupWindow를 tvYear 바로 위에 표시
        popupWindow.showAtLocation(binding.tvYear, Gravity.NO_GRAVITY, 40, y - popupView.height - binding.tvYear.height - 20)

        // 팝업 외부 터치 이벤트 감지 및 닫기
        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss() // 외부 터치 시 닫기
            isPopupVisible = false
            true
        }
    }

    private fun updateYear(year: String) {
        selectedYear = year
        selectedMonth = null
        binding.tvYear.text = year
        monthAdapter.clearSelection()
        filterImagesByYearAndMonth(selectedYear, selectedMonth)
    }

    private fun onMonthSelected(month: String) {
        selectedMonth = month.padStart(2, '0')
        filterImagesByYearAndMonth(selectedYear, selectedMonth)
    }

    private fun filterImagesByYearAndMonth(year: String?, month: String?) {
        val formattedMonth = month?.padStart(2, '0') // "1" → "01", "6" → "06"

        // 이미지 및 태그 데이터 필터링
        val filteredImages = imagesByDate.filter { (dateKey, _) ->
            val parts = dateKey.split("-") // 예: "2024-06월 15일"
            if (parts.size < 2) return@filter false // 예외 방지

            val dateYear = parts[0] // "2024"
            val dateMonth = parts[1].split("월")[0].trim().padStart(2, '0') // "06"

            (year == null || dateYear == year) && (formattedMonth == null || dateMonth == formattedMonth)
        }

        val filteredTags = tagsByDate.filter { (dateKey, _) ->
            val parts = dateKey.split("-")
            if (parts.size < 2) return@filter false // 예외 방지

            val dateYear = parts[0]
            val dateMonth = parts[1].split("월")[0].trim().padStart(2, '0')

            (year == null || dateYear == year) && (formattedMonth == null || dateMonth == formattedMonth)
        }

        Log.d("TagBoardFragment", "필터링된 연도: $year, 월: $month")
        Log.d("TagBoardFragment", "필터링된 이미지 개수: ${filteredImages.size}")
        Log.d("TagBoardFragment", "필터링된 태그 개수: ${filteredTags.size}")

        // RecyclerView 업데이트
        setupRecyclerView(filteredImages, filteredTags)
    }


    private fun getAvailableYears(): List<String> {
        val projection = arrayOf(
            MediaStore.Images.Media.DATE_TAKEN
        )

        val yearSet = mutableSetOf<String>()

        val query = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        query?.use { cursor ->
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val dateTaken = cursor.getLong(dateTakenColumn)
                val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(dateTaken))

                // "1970" 제외
                if (year != "1970") {
                    yearSet.add(year)
                }
            }
        }

        return yearSet.sortedDescending() // 내림차순 정렬
    }

    private fun onTagClicked(selectedTag: String) {
        Log.d("TagBoardFragment", "Clicked tag: $selectedTag")

        val filteredImages = imagesByDate
            .filterKeys { dateKey -> tagsByDate[dateKey]?.contains(selectedTag) == true }
            .values.flatten()
            .distinct() // ✅ 중복 방지

        Log.d("TagBoardFragment", "Filtered images count: ${filteredImages.size}")

        val bundle = Bundle().apply {
            putString("selectedTag", selectedTag)
            putStringArrayList("filteredImages", ArrayList(filteredImages))
        }

        findNavController().navigate(R.id.action_recordFragment_to_tagImgFragment, bundle)
    }


    companion object {
        fun newInstance(): TagBoardFragment {
            return TagBoardFragment()
        }
    }

}
