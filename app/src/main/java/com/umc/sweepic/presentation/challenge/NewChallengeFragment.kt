package com.umc.sweepic.presentation.challenge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentChallengeListBinding
import com.umc.sweepic.domain.model.request.challenge.LocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationInfoRequestModel
import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class NewChallengeFragment: BaseFragment<FragmentChallengeListBinding>(R.layout.fragment_challenge_list){
    private lateinit var challengeAdapter: ChallengeAdapter
    private val viewModel: ChallengeViewModel by viewModels()

    override fun initObserver() {
        viewModel.locationInfoList.observe(viewLifecycleOwner) { locationList ->
            if (!locationList.isNullOrEmpty()) {
                createLocationChallenge(locationList)
            }
        }

        viewModel.challengeList.observe(viewLifecycleOwner) { challenges ->
            challengeAdapter.submitList(challenges)
        }

        viewModel.challengeCreated.observe(viewLifecycleOwner) { isCreated ->
            if (isCreated) {
                viewModel.fetchGetChallenge()
            }
        }

    }

    override fun initView() {
        setupRecyclerView()
        checkAndLoadGalleryImages()
        viewModel.fetchGetChallenge()
    }

    private fun checkAndLoadGalleryImages() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (requiredPermissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }) {
            loadGalleryImagesAndCallApi() // 권한이 있으면 바로 실행
        } else {
            requestPermissionsLauncher.launch(requiredPermissions)
        }
    }

    // 권한 요청 런처
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            loadGalleryImagesAndCallApi() // 권한 승인 시 다시 실행
        } else {
            Toast.makeText(requireContext(), "갤러리 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        challengeAdapter = ChallengeAdapter(
            onAcceptChallenge = { challengeId ->
                viewModel.fetchAcceptChallenge(challengeId) // 챌린지 수락 API 호출
            }
        )
        binding.rvChallengeContainer.layoutManager = LinearLayoutManager(context)  // 레이아웃 매니저 설정
        binding.rvChallengeContainer.adapter = challengeAdapter
    }

    private fun loadGalleryImagesAndCallApi() {
        val galleryImages = viewModel.loadImages() // 갤러리에서 모든 이미지 불러오기

        val now = Calendar.getInstance() // 현재 시간 가져오기
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)

        // 30분 단위로 최근 시간 계산
        val startMinute = if (currentMinute < 30) 0 else 30
        val startTime = Calendar.getInstance().apply {
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis // 밀리초 단위 변환

        val endTime = startTime + (30 * 60 * 1000) // 30분 후의 밀리초 값

        Log.d("ChallengeViewModel", "필터링할 시간 범위: ${Date(startTime)} ~ ${Date(endTime)}")

        // `finalDate` 기준으로 필터링
        val filteredImages = galleryImages.filter { image ->
//            Log.d("GalleryFilter", "사진 ID=${image.id}, finalDate=${Date(image.addedDate.time)}, 밀리초=${image.addedDate.time}")
            image.addedDate.time in startTime..endTime // **밀리초 단위 비교**
        }

        if (filteredImages.isNotEmpty()) {
            Log.d("ChallengeViewModel", "최근 30분 사진 ${filteredImages.size}개 발견, fetchSweepImages() 호출")

            // `fetchSweepImages()`를 먼저 호출 (새로 생성된 이미지들의 ID를 전송)
            viewModel.fetchSweepImages(filteredImages) {
                // `fetchSweepImages()` 완료 후 `fetchObserveLocationChallenge()` 실행
                startObserveLocationChallenge(filteredImages)
            }
        } else {
            Log.d("ChallengeViewModel", "최근 30분 동안 찍힌 사진이 없음")
        }
    }

    // `fetchObserveLocationChallenge()` 호출을 분리하여, `fetchSweepImages()` 완료 후 실행되도록 함
    private fun startObserveLocationChallenge(filteredImages: List<Gallery>) {
        val requestList = filteredImages.map { image ->
            LocationInfoRequestModel(
                timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ko", "KR")).format(image.addedDate),
                latitude = image.latitude,
                longitude = image.longitude,
                displayName = image.name,
                id = image.id.toString()
            )
        }

        Log.d("ChallengeViewModel", "fetchObserveLocationChallenge() 실행, request 개수: ${requestList.size}")
        viewModel.fetchObserveLocationChallenge(requestList)
    }

    private fun createLocationChallenge(locationResponse: List<LocationInfoResponseModel>) {
        val requiredCount = locationResponse.size
        val location = locationResponse[0].location

        val request = LocationChallengeRequestModel(
            required = requiredCount,
            location = location,
            context = "여행 위치 챌린지 생성"
        )

        viewModel.fetchCreateLocationChallenge(request)
    }

}
