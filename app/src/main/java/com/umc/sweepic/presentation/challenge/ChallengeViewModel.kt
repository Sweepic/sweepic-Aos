package com.umc.sweepic.presentation.challenge

import android.content.Context
import android.content.SharedPreferences
import android.media.ExifInterface
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.sweepic.domain.model.request.challenge.CreateLocationChallengeRequestModel
import com.umc.sweepic.presentation.challenge.adapter.Challenge
import com.umc.sweepic.presentation.challenge.adapter.Challenge.Companion.toChallengeList
import com.umc.sweepic.domain.model.request.challenge.CreateWeeklyChallengeRequestModel
import com.umc.sweepic.domain.model.request.challenge.LocationLogicRequestModel
import com.umc.sweepic.domain.model.response.challenge.toChallenge
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _newChallenges = MutableLiveData<List<Challenge>>()
    val newChallenges: LiveData<List<Challenge>> get() = _newChallenges

    private val _inProgressChallenges = MutableLiveData<List<Challenge>>()
    val inProgressChallenges: LiveData<List<Challenge>> get() = _inProgressChallenges

    private val sentPhotoIds = mutableSetOf<String>()

    init {
        loadChallenges()
        loadSentPhotoIds()
    }

    // 📌 기존에 보낸 사진 ID 불러오기 (중복 방지)
    private fun loadSentPhotoIds() {
        val savedIds = sharedPreferences.getStringSet("SENT_PHOTO_IDS", emptySet()) ?: emptySet()
        sentPhotoIds.addAll(savedIds)
        Log.d("ChallengeViewModel", "📂 기존에 보낸 사진 로드 완료: ${sentPhotoIds.size}개")
    }

    // 📌 보낸 사진 ID 저장
    private fun saveSentPhotoIds() {
        sharedPreferences.edit().putStringSet("SENT_PHOTO_IDS", sentPhotoIds).apply()
        Log.d("ChallengeViewModel", "✅ 보낸 사진 목록 저장 완료")
    }


    //챌린지 목록 불러오기
    // ✅ 챌린지 목록 불러오기 (기존 + 주간 챌린지)
    fun loadChallenges() {
        viewModelScope.launch {
            repository.getUserChallenge()
                .onSuccess { challengeResponseList ->
                    val challengeList = challengeResponseList.toChallengeList()
                    val newChallengeList = challengeList.filter { it.status == 1 }
                    val inProgressChallengeList = challengeList.filter { it.status == 2 }

                    _newChallenges.postValue(newChallengeList)
                    _inProgressChallenges.postValue(inProgressChallengeList)

                    // 📌 주간 챌린지도 함께 불러오기!
                    loadWeeklyChallenge()


                }
                .onFailure { error ->
                    Log.e("ChallengeViewModel", "❌ getUserChallenge API 호출 실패", error)
                }
        }
    }

    // ✅ 주간 챌린지 불러오기
    private fun loadWeeklyChallenge() {
        val lastChallengeId = sharedPreferences.getString("LAST_WEEKLY_CHALLENGE_ID", null) ?: return

        viewModelScope.launch {
            repository.getWeeklyChallenge(lastChallengeId)
                .onSuccess { response ->
                    val challenge = response.toChallenge()

                    // ✅ SharedPreferences에서 저장된 사진 경로 가져오기
                    val jsonPhotoPaths = sharedPreferences.getString("WEEKLY_CHALLENGE_PHOTOS_${response.id}", "[]")
                    val photoPaths: List<String> = Gson().fromJson(jsonPhotoPaths, object : TypeToken<List<String>>() {}.type)

                    Log.e("ChallengeViewModel", "📸 불러온 사진 리스트: $photoPaths")

                    // 📌 주간 챌린지 객체에 사진 추가
                    val updatedChallenge = challenge.copy(photos = photoPaths)

                    if (updatedChallenge.photos.isEmpty()) {
                        Log.e("ChallengeViewModel", "❌ photos 리스트가 비어 있음! SharedPreferences에서 데이터가 제대로 불러와지지 않았을 가능성")
                    }

                    if (updatedChallenge.status == 1) {
                        _newChallenges.postValue((_newChallenges.value ?: emptyList()) + updatedChallenge)
                        Log.e("ChallengeViewModel", "📌 New Challenge에 추가됨: ${updatedChallenge.title}, 사진 개수: ${updatedChallenge.photos.size}")
                    } else if (updatedChallenge.status == 2) {
                        _inProgressChallenges.postValue((_inProgressChallenges.value ?: emptyList()) + updatedChallenge)
                        Log.e("ChallengeViewModel", "📌 In-Progress Challenge에 추가됨: ${updatedChallenge.title}, 사진 개수: ${updatedChallenge.photos.size}")
                    }
                }
                .onFailure { error ->
                    Log.e("ChallengeViewModel", "❌ getWeeklyChallenge API 호출 실패", error)
                }
        }
    }


    //주간 챌린지 생성
    fun createWeeklyChallenge(context: Context) {
        viewModelScope.launch {
            try {
                val photoCountByDate = fetchPhotoCountByDate(context)
                val latestDate = photoCountByDate.keys.maxOrNull() ?: return@launch
                val required = photoCountByDate[latestDate] ?: 0

                val request = CreateWeeklyChallengeRequestModel(
                    required = required,
                    challengeDate = latestDate,
                    context = "자동 생성된 주간 챌린지"
                )

                repository.createWeeklyChallenge(request)
                    .onSuccess { response ->
                        val photoPaths = fetchPhotoPathsByDate(context, latestDate)

                        Log.e("ChallengeViewModel", "📸 저장할 사진 목록: $photoPaths")

                        val gson = Gson()
                        val jsonPhotoPaths = gson.toJson(photoPaths)

                        sharedPreferences.edit()
                            .putString("WEEKLY_CHALLENGE_PHOTOS_${response.id}", jsonPhotoPaths)
                            .putString("LAST_WEEKLY_CHALLENGE_ID", response.id)
                            .putString("LAST_WEEKLY_CHALLENGE_DATE", latestDate)
                            .apply()

                        Log.e("ChallengeViewModel", "✅ SharedPreferences에 사진 저장 완료! ID: ${response.id}")

                        loadWeeklyChallenge()  // 챌린지 목록 다시 불러오기
                    }
                    .onFailure { error ->
                        Log.e("ChallengeViewModel", "❌ 챌린지 생성 실패", error)
                    }
            } catch (e: Exception) {
                Log.e("ChallengeViewModel", "❌ createWeeklyChallenge 예외 발생", e)
            }
        }
    }


    //사진 중복 전송 방지
    private fun fetchPhotoCountByDate(context: Context): Map<String, Int> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // 날짜 포맷 맞추기
        dateFormat.timeZone = TimeZone.getDefault() // ✅ 로컬 시간 적용

        val photoCountByDate = mutableMapOf<String, Int>()

        val projection = arrayOf(MediaStore.Images.Media.DATE_TAKEN)
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        if (cursor == null) {
            Log.e("ChallengeViewModel", "❌ MediaStore에서 사진을 가져올 수 없음 (cursor == null)")
            return emptyMap()
        }

        cursor.use {
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val timestamp = it.getLong(dateColumn)
                val dateStr = dateFormat.format(Date(timestamp)) // 🔥 날짜 변환 후 비교

               // Log.e("ChallengeViewModel", "📸 감지된 사진 날짜: $dateStr, 타임스탬프: $timestamp")

                photoCountByDate[dateStr] = (photoCountByDate[dateStr] ?: 0) + 1
            }
        }

        Log.e("ChallengeViewModel", "📷 최종 날짜별 사진 개수: $photoCountByDate")
        return photoCountByDate
    }


    private fun fetchPhotoPathsByDate(context: Context, targetDate: String): List<String> {
        Log.e("ChallengeViewModel", "🚀 fetchPhotoPathsByDate() 실행됨! 타겟 날짜: $targetDate")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        val photoPaths = mutableListOf<String>()
        val projection = arrayOf(
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

        if (cursor == null) {
            Log.e("ChallengeViewModel", "❌ MediaStore에서 사진을 가져올 수 없음 (cursor == null)")
            return emptyList()
        }

        cursor.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val filePath = it.getString(dataColumn)
                val timestamp = it.getLong(dateColumn)
                val dateStr = dateFormat.format(Date(timestamp))

                Log.e("ChallengeViewModel", "📸 사진 파일: $filePath, 날짜: $dateStr, 타겟 날짜: $targetDate")

                if (dateStr.trim() == targetDate.trim()) {
                    photoPaths.add(filePath)
                }
            }
        }

        Log.e("ChallengeViewModel", "📸 [$targetDate] 날짜의 사진 목록: $photoPaths")
        return photoPaths
    }



    //주간 챌린지 불러오기
    fun getWeeklyChallenge(challengeId: String) {
        viewModelScope.launch {
            repository.getWeeklyChallenge(challengeId)
                .onSuccess { response ->
                    Log.e("ChallengeViewModel", "✅ 주간 챌린지 불러오기 성공: ${response.title}")

                    val gson = Gson()
                    val jsonPhotoPaths = sharedPreferences.getString("WEEKLY_CHALLENGE_PHOTOS_${response.id}", "[]")
                    val photoPaths: List<String> = gson.fromJson(jsonPhotoPaths, object : TypeToken<List<String>>() {}.type)

                    Log.e("ChallengeViewModel", "📸 저장된 사진 경로 리스트: $photoPaths")

                    val challenge = response.toChallenge().copy(photos = photoPaths)

                    if (challenge.status == 1) {
                        _newChallenges.postValue(listOf(challenge))
                        Log.e("ChallengeViewModel", "📌 새로운 챌린지에 추가됨: ${challenge.title}")
                    } else if (challenge.status == 2) {
                        _inProgressChallenges.postValue(listOf(challenge))
                        Log.e("ChallengeViewModel", "📌 진행 중 챌린지에 추가됨: ${challenge.title}")
                    }
                }
                .onFailure { error ->
                    Log.e("ChallengeViewModel", "❌ getWeeklyChallenge API 호출 실패", error)
                }
        }
    }


    /*    // 📌 1. getLocationLogic API - 기기 내 사진 정보 전달 & location 받기
        fun fetchAndProcessLocationPhotos(context: Context) {
            viewModelScope.launch {
                Log.d("ChallengeViewModel", "🚀 fetchAndProcessLocationPhotos 실행됨")

                val locationPhotos = fetchDevicePhotosWithLocation(context)
                if (locationPhotos.isEmpty()) {
                    Log.e("ChallengeViewModel", "❌ 위치 정보가 포함된 사진이 없음")
                    return@launch
                }

                val newPhotos = locationPhotos.filter { it.id !in sentPhotoIds }
                if (newPhotos.isEmpty()) {
                    Log.d("ChallengeViewModel", "⚠️ 모든 사진이 이미 전송됨. 새로운 요청 없음")
                    return@launch
                }

                repository.getLocationLogic(newPhotos)
                    .onSuccess { response ->
                        Log.d("ChallengeViewModel", "📍 getLocationLogic 응답 개수: ${response.size}")

                        if (response.isEmpty()) {
                            Log.e("ChallengeViewModel", "❌ getLocationLogic 응답이 비어 있음")
                            return@onSuccess
                        }

                        val location = response.firstOrNull()?.location
                        if (location.isNullOrEmpty()) {
                            Log.e("ChallengeViewModel", "❌ 서버 응답에 location 값 없음")
                            return@onSuccess
                        }

                        // 🔥 filePath 대신 displayName 사용 가능성 있음
                        val photoPaths = newPhotos.map { it.displayName }
                        Log.d("ChallengeViewModel", "✅ 챌린지 생성 요청: location = $location, 사진 리스트: $photoPaths")

                        sentPhotoIds.addAll(newPhotos.map { it.id })
                        saveSentPhotoIds()

                        createLocationChallenge(location, photoPaths.size)
                    }
                    .onFailure { error ->
                        Log.e("ChallengeViewModel", "❌ getLocationLogic API 요청 실패", error)
                    }
            }
        }

    // 📌 위치 기반 챌린지 생성
    private suspend fun createLocationChallenge(location: String, photoCount: Int) {
        Log.d("ChallengeViewModel", "📸 챌린지 생성 요청 - 위치: $location, 사진 개수: $photoCount")

        val request = CreateLocationChallengeRequestModel(
            context = "위치 기반 챌린지 자동 생성",
            location = location,
            required = photoCount
        )

        repository.createLocationChallenge(request)
            .onSuccess { response ->
                Log.d("ChallengeViewModel", "🎉 위치 기반 챌린지 생성 성공! ID: ${response.id}")

                getCreatedLocationChallenge(response.id)
            }
            .onFailure { error ->
                Log.e("ChallengeViewModel", "❌ createLocationChallenge API 요청 실패", error)
            }
    }


    // 📌 3. getLocationChallenge API - 생성된 챌린지를 UI에 표시
    private fun getCreatedLocationChallenge(challengeId: String) {
        viewModelScope.launch {
            repository.getLocationChallenge(challengeId)
                .onSuccess { response ->
                    Log.d("ChallengeViewModel", "✅ 챌린지 불러오기 성공: ${response.title}")

                    val updatedChallenge = response.toChallenge()

                    if (updatedChallenge.status == 1) {
                        _newChallenges.postValue((_newChallenges.value ?: emptyList()) + updatedChallenge)
                    } else if (updatedChallenge.status == 2) {
                        _inProgressChallenges.postValue((_inProgressChallenges.value ?: emptyList()) + updatedChallenge)
                    }
                }
                .onFailure { error ->
                    Log.e("ChallengeViewModel", "❌ getLocationChallenge API 호출 실패", error)
                }
        }
    }*/

    // 📌 기기 내 사진 정보 가져오기 (위치 정보 포함)
    private fun fetchDevicePhotosWithLocation(context: Context): List<LocationLogicRequestModel> {
        val photosWithLocation = mutableListOf<LocationLogicRequestModel>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA // ✅ filePath에 해당하는 데이터 확인
        )

        val cursor = context.contentResolver.query(
            uri, projection, null, null,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) // ✅ filePath 대응
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val photoId = it.getString(idColumn)
                val filePath = it.getString(dataColumn) // ✅ 실제 파일 경로
                val timestamp = it.getLong(dateColumn)

                if (sentPhotoIds.contains(photoId)) {
                    Log.d("ChallengeViewModel", "⚠️ 중복 사진 제외 - ID: $photoId")
                    continue
                }

                val exif = try { ExifInterface(filePath) } catch (e: Exception) { null }
                val latLong = FloatArray(2)

                if (exif?.getLatLong(latLong) == true) {
                    photosWithLocation.add(
                        LocationLogicRequestModel(
                            id = photoId,
                            displayName = filePath, // ✅ displayName에 filePath 저장
                            latitude = latLong[0].toDouble(),
                            longitude = latLong[1].toDouble(),
                            timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date(timestamp))
                        )
                    )
                }
            }
        }
        Log.d("ChallengeViewModel", "🚀 fetchDevicePhotosWithLocation 실행됨")

        // ✅ 사진 개수 및 정보 확인
        Log.d("ChallengeViewModel", "📸 위치 정보 포함된 사진 개수: ${photosWithLocation.size}")

        photosWithLocation.forEach {
            Log.d("ChallengeViewModel", "📍 사진 ID: ${it.id}, 경로: ${it.displayName}, 위치: (${it.latitude}, ${it.longitude})")
        }

        return photosWithLocation
    }

    fun acceptChallenge(challengeId: String) {
        viewModelScope.launch {
            Log.d("ChallengeViewModel", "🚀 챌린지 상태 확인 중...")

            // 📌 최신 챌린지 목록 가져오기
            loadChallenges()

            // 📌 최신 데이터에서 해당 챌린지 상태 확인
            val challenge = (_newChallenges.value ?: listOf()).find { it.id == challengeId }

            if (challenge == null) {
                Log.e("ChallengeViewModel", "❌ 챌린지를 찾을 수 없음: ID $challengeId")
                return@launch
            }

            if (challenge.status != 1) { // ✅ 새로운 챌린지만 수락 가능
                Log.e("ChallengeViewModel", "❌ 챌린지가 이미 진행 중이거나 완료됨: ${challenge.id}")
                return@launch
            }

            // 📌 상태가 유효하면 accept API 요청
            repository.acceptChallenge(challengeId)
                .onSuccess {
                    Log.d("ChallengeViewModel", "🎉 챌린지 수락 성공! ID: $challengeId")
                    loadChallenges() // UI 업데이트
                }
                .onFailure { error ->
                    Log.e("ChallengeViewModel", "❌ acceptChallenge API 요청 실패", error)
                }
        }
    }

}


