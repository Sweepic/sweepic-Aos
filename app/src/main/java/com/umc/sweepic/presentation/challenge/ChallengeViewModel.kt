package com.umc.sweepic.presentation.challenge

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.R
import com.umc.sweepic.domain.model.Challenge
import com.umc.sweepic.domain.model.request.challenge.LocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationInfoRequestModel
import com.umc.sweepic.domain.model.response.challenge.CommonChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.GetChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.LocationInfoResponseModel
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import com.umc.sweepic.domain.repository.sweep.TrashRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository,
    private val galleryRepository: GalleryRepository
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



    var imageListForChallenge: List<Gallery> = emptyList()

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

    // 챌린지 조회 API
    fun fetchGetChallenge() {
        viewModelScope.launch {
            repository.fetchGetChallenge()
                .onSuccess { response ->
                    val newChallenges = response
                        .filter { it.status == 1 } // status가 1인 챌린지만 필터링
                        .map { challenge ->
                            ChallengeWithImages(challenge, imageListForChallenge.take(3)) // 각 챌린지에 대해 이미지 3개 포함
                        }
                    val inProgressChallenges = response
                        .filter { it.status == 2 } // status가 2인 것 (진행 중 챌린지)
                        .map { challenge ->
                            ChallengeWithImages(challenge, imageListForChallenge.take(3))
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

//    fun fetchGetLocationChallenge(challengeId: String) {
//        viewModelScope.launch {
//            repository.fetchGetLocationChallenge(challengeId)
//                .onSuccess { response ->
//                    if (response.status == 1) {
//                        val challengeWithImages = ChallengeWithImages(response, imageListForChallenge.take(3))
//                        val currentList = _challengeList.value.orEmpty().toMutableList()
//                        currentList.add(challengeWithImages)
//                        _challengeList.value = currentList
//                        Log.d("ChallengeViewModel", "챌린지 조회 성공: $response")
//                    } else {
//                        Log.d("ChallengeViewModel", "챌린지 상태가 1이 아니므로 추가되지 않음: $response")
//                    }
//                }
//                .onFailure { exception ->
//                    Log.e("ChallengeViewModel", "챌린지 조회 실패: ${exception.message}")
//                }
//        }
//    }
}
data class ChallengeWithImages(
    val challenge: GetChallengeResponseModel,
    val images: List<Gallery>
)