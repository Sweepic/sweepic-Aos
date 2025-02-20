package com.umc.sweepic.presentation.challenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.ChallengeWithImages
import com.umc.sweepic.domain.model.request.challenge.LocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationInfoRequestModel
import com.umc.sweepic.domain.model.request.sweep.UpdateImageRequestModel
import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel
import com.umc.sweepic.domain.model.response.sweep.GetUserInformationResponseModel
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import com.umc.sweepic.domain.repository.sweep.MypageRepository
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository,
    private val galleryRepository: GalleryRepository,
    private val sweepRepository: SweepRepository,
    private val mypageRepository: MypageRepository
): ViewModel() {
    private val _locationInfoList = MutableLiveData<List<LocationInfoResponseModel>>()
    val locationInfoList: LiveData<List<LocationInfoResponseModel>> get() = _locationInfoList

    private val _challengeList = MutableLiveData<List<ChallengeWithImages>>()
    val challengeList: LiveData<List<ChallengeWithImages>> get() = _challengeList

    // 챌린지 생성 됐는지 판별
    private val _challengeCreated = MutableLiveData<Boolean>()
    val challengeCreated: LiveData<Boolean> get() = _challengeCreated

    private val _inProgressChallengeList = MutableLiveData<List<ChallengeWithImages>>()
    val inProgressChallengeList: LiveData<List<ChallengeWithImages>> get() = _inProgressChallengeList

    private var lastSentImageIds: List<String> = emptyList()

    private val _userInfo = MutableLiveData<GetUserInformationResponseModel>() // 사용자 정보 LiveData
    val userInfo: LiveData<GetUserInformationResponseModel> get() = _userInfo

    private val _totalImageCount = MutableLiveData<Int>()
    val totalImageCount: LiveData<Int> get() = _totalImageCount

    private val _todayImageCount = MutableLiveData<Int>().apply { value = 0 } // 기본값 0
    val todayImageCount: LiveData<Int> get() = _todayImageCount

    private val _recentImageCount = MutableLiveData<Int>()
    val recentImageCount: LiveData<Int> get() = _recentImageCount


    // 갤러리 이미지 로드
    fun loadImages(): List<Gallery> {
        return galleryRepository.getAllGalleryImagesDesc()
    }

    // 위치 챌린지 판별 API
    fun fetchObserveLocationChallenge(request: List<LocationInfoRequestModel>) {
        viewModelScope.launch {
            repository.fetchObserveLocationChallenge(request)
                .onSuccess { response ->
                    _locationInfoList.value = response
                    Log.d("ChallengeViewModel", "Location info success: $response")

                    // 성공 시 보낸 이미지 ID 저장
                    lastSentImageIds = request.map { it.id }
                }
                .onFailure { exception ->
                    Log.e("ChallengeViewModel", "Location info failed: ${exception.message}")
                }
        }
    }

    // 챌린지 생성 API
    fun fetchCreateLocationChallenge(request: LocationChallengeRequestModel) {
        viewModelScope.launch {
            repository.fetchCreateLocationChallenge(request)
                .onSuccess { response ->
                    Log.d("ChallengeViewModel", "챌린지 생성 성공: $response")
                    _challengeCreated.value = true // 챌린지 생성 완료 표시

                    // 생성된 챌린지 ID를 이용해 fetchUploadChallengeImage 호출
                    fetchUploadChallengeImage(response.id, lastSentImageIds) {
                        fetchGetChallenge() // 이미지 업로드 완료 후 챌린지 목록 갱신
                    }
                }
                .onFailure { exception ->
                    Log.e("ChallengeViewModel", "챌린지 생성 실패: ${exception.message}")
                }
        }
    }

    // 챌린지 수락 API
    fun fetchAcceptChallenge(challengeId: String) {
        viewModelScope.launch {
            repository.fetchAcceptChallenge(challengeId)
                .onSuccess { response ->
                    Log.d("ChallengeViewModel", "챌린지 수락 성공: $response")
                    // 수락한 챌린지를 리스트에서 즉시 제거 (RecyclerView 즉시 반영)
                    val updatedList = _challengeList.value.orEmpty().filterNot { it.challenge.id == challengeId }
                    _challengeList.postValue(updatedList)

                    // 최신 챌린지 목록을 다시 불러와 동기화
                    fetchGetChallenge()
                }
                .onFailure { exception ->
                    Log.e("ChallengeViewModel", "챌린지 수락 실패: ${exception.message}")
                }
        }
    }

    // 챌린지에 이미지 업로드 API
    fun fetchUploadChallengeImage(challengeId: String, imageIds: List<String>, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.fetchUploadChallengeImage(challengeId, imageIds)
                .onSuccess { response ->
                    Log.d("ChallengeViewModel", "이미지 업로드 성공: $response")
                    onComplete()
                }
                .onFailure { exception ->
                    Log.e("ChallengeViewModel", "이미지 업로드 실패: ${exception.message}")
                }
        }
    }

    // 챌린지 조회 API
    fun fetchGetChallenge() {
        viewModelScope.launch {
            repository.fetchGetChallenge()
                .onSuccess { response ->
                    val localGalleryImages = loadImages()

                    val newChallenges = response
                        .filter { it.status == 1 } // status가 1인 챌린지만 필터링
                        .map { challenge ->
                            val matchedImages = challenge.images.mapNotNull { imageId ->
                                localGalleryImages.find { it.id.toString() == imageId }
                            }.take(3) // 3장만 선택

                            ChallengeWithImages(challenge, matchedImages)
                        }
                    val inProgressChallenges = response
                        .filter { it.status == 2 } // status가 2인 것 (진행 중 챌린지)
                        .map { challenge ->
                            val matchedImages = challenge.images.mapNotNull { imageId ->
                                localGalleryImages.find { it.id.toString() == imageId }
                            }.take(3) // 3장만 선택

                            ChallengeWithImages(challenge, matchedImages)
                        }

                    _challengeList.postValue(newChallenges) // 기존 리스트 업데이트
                    _inProgressChallengeList.postValue(inProgressChallenges) // 진행 중 챌린지 리스트 업데이트
                    Log.d("ChallengeViewModel", "챌린지 조회 성공: $newChallenges")
                    Log.d("ChallengeViewModel", "진행 중 챌린지 조회 성공: $inProgressChallenges")
                }
                .onFailure { exception ->
                    Log.e("ChallengeViewModel", "챌린지 조회 실패: ${exception.message}")
                }
        }
    }

    fun fetchSweepImages(images: List<Gallery>, onComplete: () -> Unit) {
        viewModelScope.launch {
            images.forEach { image -> // 하나씩 호출
                val request = UpdateImageRequestModel(
                    timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ko", "KR")).format(image.addedDate),
                    mediaId = image.id.toString()
                )

                sweepRepository.fetchSweepImages(request)
                    .onSuccess { response ->
                        Log.d("ChallengeViewModel", "fetchSweepImages 성공: ${response.imageId}")
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "fetchSweepImages 실패: ${exception.message}")
                    }
            }
            onComplete() // 모든 요청이 끝난 후 실행
        }
    }

    fun getUserInformation() {
        viewModelScope.launch {
            mypageRepository.getUserInformation()
                .onSuccess { response ->
                    _userInfo.postValue(response)
                    Log.d("ChallengeViewModel", "getUserInformation 성공: $response")

                    updateRecentImageCount()
                }
                .onFailure { exception ->
                    Log.d("ChallengeViewModel", "getUserInformation 성공: ${exception.message}")
                }
        }
    }

    fun loadImageCounts() {
        viewModelScope.launch {
            val allImages = galleryRepository.getAllGalleryImagesDesc() // 모든 갤러리 이미지 가져오기

            // 총 사진 개수 업데이트
            _totalImageCount.postValue(allImages.size)

            // 오늘 날짜의 시작과 끝 시간 계산
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = calendar.timeInMillis // 오늘 00:00:00
            val endOfDay = startOfDay + TimeUnit.DAYS.toMillis(1) - 1 // 오늘 23:59:59

            // 오늘 추가된 사진 개수 필터링
            val todayImages = allImages.filter { it.addedDate.time in startOfDay..endOfDay }
            _todayImageCount.postValue(todayImages.size)

            updateRecentImageCount()

        }
    }

    fun updateRecentImageCount() {
        val totalCount = _totalImageCount.value ?: 0
        val targetCount = _userInfo.value?.goalCount ?: 0
        _recentImageCount.postValue(totalCount - targetCount)  // 🔥 tvRecentNumber 값 업데이트
    }
}