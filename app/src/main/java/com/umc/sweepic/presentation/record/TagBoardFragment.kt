package com.umc.sweepic.presentation.record

import android.Manifest
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
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
    private lateinit var dateAdapter: DateRecyclerAdapter
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1001


    override fun initObserver() {
        // 필요하면 데이터 관찰 추가
    }

    override fun initView() {
        setupDateRecyclerView()
        checkPermissions()
    }

    private fun checkPermissions() {
        val requiredPermissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상
            listOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Android 13 미만
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val deniedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isNotEmpty()) {
            requestPermissions(deniedPermissions.toTypedArray(), 1001)
        } else {
            loadImagesFromMediaStore() // 권한이 이미 허용된 경우
        }
    }
    private fun setupDateRecyclerView() {
        val dates = generateDummyDates()
        dateAdapter = DateRecyclerAdapter(dates) { selectedDate ->
            // 날짜 선택 시 처리할 로직
            Log.d("TagBoardFragment", "Selected date: $selectedDate")
        }
        binding.rcDate.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dateAdapter
        }
    }

    private fun generateDummyDates(): List<String> {
        // 예제 날짜 생성
        return listOf("2025-01-10", "2025-01-11", "2025-01-12", "2025-01-13", "2025-01-14")
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
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateTaken))

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
        binding.rcTagboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTagboard.adapter = rvAdapter
    }


    private fun generateTagsForDate(date: String): List<String> {
        return listOf("#$date Tag1", "#$date Tag2", "#$date Tag3") // 예제 태그
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

    companion object {
        fun newInstance(): TagBoardFragment {
            return TagBoardFragment()
        }
    }
}
