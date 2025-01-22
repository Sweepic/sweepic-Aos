package com.umc.sweepic.presentation.record.TagBord

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentTagBoardBinding
import com.umc.sweepic.presentation.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TagBoardFragment : BaseFragment<FragmentTagBoardBinding>(R.layout.fragment_tag_board) {

    private lateinit var rvAdapter: ImgGroupRVA
    private lateinit var monthAdapter: MonthAdapter
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001
    private var isPopupVisible = false


    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        setupYearSelector()
        setupMonthRecyclerView()
        checkPermissions()
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
            MediaStore.Images.Media.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val imagesByDate = mutableMapOf<String, MutableList<String>>()

        val tagsByDate = mutableMapOf<String, List<String>>()


        val query = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        if (query == null) {
            Log.e("TagBoardFragment", "Failed to query MediaStore")
            return
        }

        query.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = cursor.getLong(dateTakenColumn)

                // 날짜를 "yyyy-MM-dd" 형식으로 변환
                val date = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date(dateTaken))

                // 이미지 URI 생성
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                ).toString()

                // 날짜별로 그룹화
                imagesByDate.getOrPut(date) { mutableListOf() }.add(imageUri)

                //태그생성
                tagsByDate[date] = generateTagsForDate(date)}

        }

        // 데이터 확인
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
        rvAdapter = ImgGroupRVA(imagesByDate, tagsByDate)

        // 태그보드 RecyclerView 설정
        binding.rcTagboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTagboard.adapter = rvAdapter
    }


    private fun generateTagsForDate(date: String): List<String> {
        return listOf("#Tag1", "#Tag2", "#Tag3") // 예제 태그
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



    private fun updateYear(selectedYear: String) {
        binding.tvYear.text = selectedYear
        Log.d("TagBoardFragment", "Selected Year: $selectedYear")
        // 선택한 연도에 따라 데이터 필터링 로직 추가 가능
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
                yearSet.add(year)
            }
        }

        return yearSet.sortedDescending() // 내림차순 정렬
    }


    companion object {
        fun newInstance(): TagBoardFragment {
            return TagBoardFragment()
        }
    }
}
