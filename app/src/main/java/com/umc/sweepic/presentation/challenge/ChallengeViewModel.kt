package com.umc.sweepic.presentation.challenge

import android.annotation.SuppressLint
import android.content.Context
import android.media.ExifInterface
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.R
import com.umc.sweepic.data.dto.response.challenge.ChallengeGetResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeDeleteResponseDto
import com.umc.sweepic.domain.model.Challenge
import com.umc.sweepic.domain.model.request.challenge.CreateChallengeUpdateRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateLocationLogicTestRequestModel
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.response.challenge.ChallengeGetResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeDeleteResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateChallengeUpdateResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationChallengeResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateLocationLogicTestResponseModel
import com.umc.sweepic.domain.model.response.challenge.CreateWeeklyChallengeResponseModel
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository) : ViewModel(){

    private val _updateResponse = MutableLiveData<CreateChallengeUpdateResponseModel?>()
    val updateResponse: LiveData<CreateChallengeUpdateResponseModel?> get() = _updateResponse

    private val _getChallengeResponse = MutableLiveData<List<ChallengeGetResponseModel>>()
    val getChallengeResponse: LiveData<List<ChallengeGetResponseModel>> get() = _getChallengeResponse

    private val _locationTestResponse = MutableLiveData<List<CreateLocationLogicTestResponseModel>>()
    val locationTestResponse: LiveData<List<CreateLocationLogicTestResponseModel>> get() = _locationTestResponse

    private val _locationChallengeResponse = MutableLiveData<CreateLocationChallengeResponseModel?>()
    val locationChallengeResponse: LiveData<CreateLocationChallengeResponseModel?> get() = _locationChallengeResponse

    private val _weeklyChallengeResponse = MutableLiveData<CreateWeeklyChallengeResponseModel?>()
    val weeklyChallengeResponse: LiveData<CreateWeeklyChallengeResponseModel?> get() = _weeklyChallengeResponse

    private val _newChallenges = MutableLiveData<List<Challenge>>()
    val newChallenges: LiveData<List<Challenge>> get() = _newChallenges

    private val _inProgressChallenges = MutableLiveData<List<Challenge>>()
    val inProgressChallenges: LiveData<List<Challenge>> get() = _inProgressChallenges

    private val _completedChallenges = MutableLiveData<List<Challenge>>()
    val completedChallenge: LiveData<List<Challenge>> get() = _completedChallenges

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "getChallenge API 호출 시작")
                repository.fetchChallengeGet()
                    .onSuccess { response ->
                        Log.d("ChallengeViewModel", "getChallenge API 요청 성공: $response")

                        val newChallengeMap = mutableMapOf<String, Int>()
                        val inProgressChallengeMap = mutableMapOf<String, Int>()

                        val newChallenges = mutableListOf<Challenge>()
                        val inProgressChallenges = mutableListOf<Challenge>()

                        response.forEach { challengeData ->
                            val title = challengeData.title

                            when (challengeData.status) {
                                1 -> { // 상태 1: 새로운 챌린지
                                    newChallengeMap[title] = (newChallengeMap[title] ?: 0) + 1
                                }
                                2 -> { // 상태 2: 도전 중 챌린지
                                    inProgressChallengeMap[title] = (inProgressChallengeMap[title] ?: 0) + 1
                                }
                            }
                        }

                        // 새로운 챌린지 리스트 생성
                        newChallengeMap.forEach { (title, count) ->
                            newChallenges.add(
                                Challenge(
                                    num_photo = "${count}개", // 같은 타이틀 개수 사용
                                    title = title,
                                    imageResId = R.drawable.img_test
                                )
                            )
                        }

                        // 도전 중인 챌린지 리스트 생성
                        inProgressChallengeMap.forEach { (title, count) ->
                            inProgressChallenges.add(
                                Challenge(
                                    num_photo = "${count}개", // 같은 타이틀 개수 사용
                                    title = title,
                                    imageResId = R.drawable.img_test
                                )
                            )
                        }

                        _newChallenges.value = newChallenges
                        _inProgressChallenges.value = inProgressChallenges
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "getChallenge API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "getChallenge API 호출 중 예외 발생", e)
            }
        }
    }



    fun moveToInProgress(challenge: Challenge) {
        val currentNewChallenges = _newChallenges.value?.toMutableList() ?: mutableListOf()
        val currentInProgressChallenges = _inProgressChallenges.value?.toMutableList() ?: mutableListOf()

        if (currentNewChallenges.remove(challenge)) {
            currentInProgressChallenges.add(challenge)
        }

        _newChallenges.value = currentNewChallenges
        _inProgressChallenges.value = currentInProgressChallenges
    }

    fun fetchChallengeUpdateCreate(request: CreateChallengeUpdateRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "update API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "update API 호출 시작: $request")
                repository.fetchChallengeUpdate(request)
                    .onSuccess { response ->
                        _updateResponse.value = response
                        Log.d("ChallengeViewModel", "update API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _updateResponse.value = null
                        Log.e("ChallengeViewModel", "update API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "update API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchChallengeGet() {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "getChallenge API 호출 시작: ")
                repository.fetchChallengeGet()
                    .onSuccess { response ->
                        if (response.isEmpty()) {
                            Log.e("ChallengeViewModel", "getChallenge API 응답: 챌린지 없음")
                        } else {
                            _getChallengeResponse.value = response
                            Log.d("ChallengeViewModel", "getChallenge API 요청 성공: $response")
                        }
                    }
                    .onFailure { exception ->
                        _getChallengeResponse.value = emptyList()
                        Log.e("ChallengeViewModel", "getChallenge API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "getChallenge API 호출 중 예외 발생", e)
            }
        }
    }

    /*fun fetchChallengeLocationLogicTestChallengeCreate(request: CreateLocationLogicTestRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "location logic test API 요청 실패: request가 null입니다.")
            _locationTestResponse.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val requestList = listOf(request)

                Log.d("ChallengeViewModel", "location logic test API 호출 시작: $requestList")
                repository.fetchChallengeLocationLogicTestChallengeCreate(requestList)
                    .onSuccess { response ->
                        _locationTestResponse.value = response
                        Log.d("ChallengeViewModel", "location logic test API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _locationTestResponse.value = emptyList()
                        Log.e("ChallengeViewModel", "location logic test API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "location logic test API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "location API 호출 시작: $request")
                repository.fetchChallengeLocationChallengeCreate(request)
                    .onSuccess { response ->
                        _locationChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "location API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _locationChallengeResponse.value = null
                        Log.e("ChallengeViewModel", "location API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "location API 호출 중 예외 발생", e)
            }
        }
    }*/

/*    fun fetchLocationBasedPhotos() {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "fetchLocationBasedPhotos API 호출 시작")

                // ✅ Step 1. 요청할 데이터 생성
                val testRequestList = listOf(
                    CreateLocationLogicTestRequestModel(
                        id = "1001",
                        displayName = "example.jpg",
                        latitude = 37.123456,
                        longitude = 127.123456,
                        timestamp = "2025-02-11T12:01:58.997Z"
                    )
                )

                // ✅ Step 2. 위치 기반 챌린지 위한 사진 필터링 (location_logic/test API 실행)
                repository.fetchChallengeLocationLogicTestChallengeCreate(testRequestList)
                    .onSuccess { filteredPhotos ->
                        if (filteredPhotos.isNotEmpty()) {
                            Log.d("ChallengeViewModel", "필터링된 사진 리스트: $filteredPhotos")

                            // ✅ Step 3. 개별 챌린지 생성 요청 (그룹화 없이)
                            filteredPhotos.forEach { photo ->
                                val request = CreateLocationChallengeRequestModel(
                                    context = "위치 기반 챌린지 - ${photo.displayName}",
                                    location = photo.location,
                                    required = 1 // 개별 사진당 챌린지 1개
                                )

                                repository.fetchChallengeLocationChallengeCreate(request)
                                    .onSuccess { response ->
                                        _locationChallengeResponse.value = response
                                        Log.d("ChallengeViewModel", "위치 기반 챌린지 생성 성공: $response")
                                    }
                                    .onFailure { exception ->
                                        Log.e("ChallengeViewModel", "위치 기반 챌린지 생성 실패", exception)
                                    }
                            }
                        } else {
                            Log.d("ChallengeViewModel", "필터링된 사진 없음")
                        }
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "location_logic/test API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "fetchLocationBasedPhotos API 호출 중 예외 발생", e)
            }
        }
    }

    private fun extractDate(timestamp: String): String {
        return timestamp.substring(0, 10) // "2025-02-11T12:01:58.997Z" → "2025-02-11"
    }

    fun fetchLocationChallenge(filteredPhotos: List<CreateLocationLogicTestResponseModel>) {
        viewModelScope.launch {
            try {
                val request = CreateLocationChallengeRequestModel(
                    context = "위치 기반 챌린지에 도전하세요!",
                    location = filteredPhotos.first().location, // 첫 번째 필터링된 사진의 위치 사용
                    required = filteredPhotos.size // 필터링된 사진 개수만큼 required 설정
                )

                Log.d("ChallengeViewModel", "locationChallenge API 호출 시작: $request")
                repository.fetchChallengeLocationChallengeCreate(request)
                    .onSuccess { response ->
                        _locationChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "locationChallenge API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _locationChallengeResponse.value = null
                        Log.e("ChallengeViewModel", "locationChallenge API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "locationChallenge API 호출 중 예외 발생", e)
            }
        }
    }

    fun fetchWeeklyChallengeCreate(request: CreateWeeklyChallengeRequestModel?) {
        if (request == null) {
            Log.e("ChallengeViewModel", "weekly API 요청 실패: request가 null입니다.")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "weekly API 호출 시작: $request")
                repository.fetchWeeklyChallengeCreate(request)
                    .onSuccess { response ->
                        _weeklyChallengeResponse.value = response
                        Log.d("ChallengeViewModel", "weekly API 요청 성공: $response")
                    }
                    .onFailure { exception ->
                        _weeklyChallengeResponse.value = null
                        Log.e("ChallengeViewModel", "weekly API 요청 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "weekly API 호출 중 예외 발생", e)
            }
        }
    }*/

    private val _totalRequired = MutableLiveData<Int>()
    val totalRequired: LiveData<Int> get() = _totalRequired

    private val _totalRemaining = MutableLiveData<Int>()
    val totalRemaining: LiveData<Int> get() = _totalRemaining

    //challenge/update


    //location_logic/test api
    @SuppressLint("Range")
    private fun fetchDevicePhotosWithLocation(context: Context): List<CreateLocationLogicTestRequestModel> {
        val photosWithLocation = mutableListOf<CreateLocationLogicTestRequestModel>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA // 파일 경로를 가져오기 위한 컬럼
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) // 파일 경로 가져오기

            while (it.moveToNext()) {
                val id = it.getString(idColumn)
                val displayName = it.getString(nameColumn)
                val timestamp = it.getLong(dateColumn)
                val filePath = it.getString(dataColumn)

                // 📌 ExifInterface를 사용하여 위치 정보 가져오기
                val exif = try {
                    ExifInterface(filePath)
                } catch (e: Exception) {
                    Log.e("ChallengeViewModel", "❌ ExifInterface 오류: $e")
                    null
                }

                val latLong = FloatArray(2)
                val hasLocation = exif?.getLatLong(latLong) == true

                if (hasLocation) {
                    val latitude = latLong[0].toDouble()
                    val longitude = latLong[1].toDouble()

                    val photo = CreateLocationLogicTestRequestModel(
                        id = id,
                        displayName = displayName,
                        latitude = latitude,
                        longitude = longitude,
                        timestamp = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.getDefault()
                        ).format(Date(timestamp))
                    )
                    photosWithLocation.add(photo)
                }
            }
        }

        Log.d("ChallengeViewModel", "📸 위치 정보 있는 사진 개수: ${photosWithLocation.size}")
        return photosWithLocation
    }


    fun sendDevicePhotosToServer(context: Context) {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "fetchLocationLogicTest 시작")

                val locationPhotos = fetchDevicePhotosWithLocation(context)
                if (locationPhotos.isEmpty()) {
                    Log.d("ChallengeViewModel", "위치 데이터가 포함된 사진이 없음")
                    return@launch
                }

                repository.fetchChallengeLocationLogicTestChallengeCreate(locationPhotos)
                    .onSuccess { response ->
                        Log.d("ChallengeViewModel", "locationLogicTest API 성공: $response")
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "locationLogicTest API 요청 실패", exception)
                    }

            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "fetchLocationLogicTest 실행 중 예외 발생", e)
            }
        }
    }

    //location_challenge/create api
    fun fetchLocationBasedChallenge(context: Context) {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "fetchLocationBasedChallenge 시작")

                // 1️⃣ 기기의 위치 정보를 가져옴
                val devicePhotos = fetchDevicePhotosWithLocation(context)
                if (devicePhotos.isEmpty()) {
                    Log.e("ChallengeViewModel", "기기의 위치 정보를 가진 사진이 없습니다.")
                    return@launch
                }

                /// ✅ 위치 데이터 API 호출 부분에서 응답을 출력
                repository.fetchChallengeLocationLogicTestChallengeCreate(devicePhotos)
                    .onSuccess { locationTestResponse ->
                        Log.d("ChallengeViewModel", "✅ locationLogicTest API 성공: $locationTestResponse")

                        if (locationTestResponse.isEmpty()) {
                            Log.e("ChallengeViewModel", "❌ locationLogicTest API 응답이 비어 있음.")
                            return@onSuccess
                        }

                        // ✅ 응답에서 location 필드 값 출력
                        val firstResponse = locationTestResponse.firstOrNull()
                        if (firstResponse?.location == null) {
                            Log.e("ChallengeViewModel", "❌ locationLogicTest API 응답에서 location이 없습니다: $firstResponse")
                            return@onSuccess
                        }

                        val location = firstResponse.location
                        Log.d("ChallengeViewModel", "📍 받은 location 값: $location")

                        // ✅ 다음 단계 - 위치 기반 챌린지 생성 API 호출
                        val request = CreateLocationChallengeRequestModel(
                            context = "위치 기반 챌린지 자동 생성",
                            location = location,
                            required = 10
                        )

                        repository.fetchChallengeLocationChallengeCreate(request)
                            .onSuccess { challengeResponse ->
                                Log.d("ChallengeViewModel", "✅ 위치 기반 챌린지 생성 성공: $challengeResponse")
                            }
                            .onFailure { exception ->
                                Log.e("ChallengeViewModel", "❌ 위치 기반 챌린지 생성 실패", exception)
                            }
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "❌ locationLogicTest API 요청 실패", exception)
                    }

            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "fetchLocationBasedChallenge 호출 중 예외 발생", e)
            }
        }
    }

    //weekly_challenge/create
    fun sendWeeklyChallengeToServer(context: String, challengeDate: String, required: Int) {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "🚀 주간 챌린지 생성 API 호출 시작")

                val request = CreateWeeklyChallengeRequestModel(
                    context = context,
                    challengeDate = challengeDate,
                    required = required
                )

                repository.fetchWeeklyChallengeCreate(request)
                    .onSuccess { challengeResponse ->
                        Log.d("ChallengeViewModel", "✅ 주간 챌린지 생성 성공: $challengeResponse")
                    }
                    .onFailure { exception ->
                        Log.e("ChallengeViewModel", "❌ 주간 챌린지 생성 실패", exception)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "❌ 주간 챌린지 생성 API 호출 중 예외 발생", e)
            }
        }
    }


   fun fetchDevicePhotosAndCreateWeeklyChallenges(context: Context) {
        viewModelScope.launch {
            try {
                Log.d("ChallengeViewModel", "🚀 기기 내 사진 불러오기 시작")

                val projection = arrayOf(MediaStore.Images.Media.DATE_TAKEN)
                val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

                val photoDates = mutableListOf<Long>()

                cursor?.use {
                    val dateTakenColumn = it.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                    while (it.moveToNext()) {
                        val dateTaken = it.getLong(dateTakenColumn)
                        photoDates.add(dateTaken)
                    }
                }

                if (photoDates.isEmpty()) {
                    Log.d("ChallengeViewModel", "📌 기기 내의 사진이 없음")
                    return@launch
                }

                // 사진 날짜를 일주일 단위로 그룹화
                val groupedByWeek = photoDates.groupBy { date ->
                    val calendar = Calendar.getInstance().apply { timeInMillis = date }
                    calendar.get(Calendar.YEAR) to calendar.get(Calendar.WEEK_OF_YEAR)
                }

                groupedByWeek.forEach { (yearWeek, dates) ->
                    val earliestDate = dates.minOrNull() ?: return@forEach
                    val challengeDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date(earliestDate))

                    // 주간 챌린지 서버에 요청
                    sendWeeklyChallengeToServer("이번 주 사진 챌린지!", challengeDate, dates.size)
                }

            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "❌ 기기 내 사진 불러오는 중 예외 발생", e)
            }
        }
    }

}


