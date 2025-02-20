package com.umc.sweepic.presentation.record.memo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.response.sweep.MemoFolderDetailModel
import com.umc.sweepic.domain.repository.sweep.MemoRepository
import com.umc.sweepic.presentation.record.memo.MemoFolder.Companion.toMemoFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoFolderViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {

    private val _memoFolders = MutableLiveData<List<MemoFolder>>()
    val memoFolders: LiveData<List<MemoFolder>> get() = _memoFolders

    // 메모 목록 가져오기
    fun fetchMemoFolders() {
        viewModelScope.launch {
            memoRepository.recordMemoList()
                .onSuccess { data ->
                    Log.d("MemoFolderViewModel", "API 응답 성공: ${data.data}") // ✅ 응답 데이터 확인
                    _memoFolders.postValue(data.data.map { it.toMemoFolder() })
                }
                .onFailure { error ->
                    Log.e("MemoFolderViewModel", "API 응답 실패: ${error.message}") // ✅ 오류 로그 확인
                    _memoFolders.postValue(emptyList()) // 실패 시 빈 리스트 반환
                }
        }
    }

    private val _memoFolderDetail = MutableLiveData<MemoFolderDetailModel?>()
    val memoFolderDetail: LiveData<MemoFolderDetailModel?> get() = _memoFolderDetail

    fun fetchMemoFolderDetails(folderId: Long, onFetched: (List<String>) -> Unit = {}) {
        viewModelScope.launch {
            memoRepository.fetchMemoFolderDetails(folderId)
                .onSuccess { data ->
                    val safeImages = data.images ?: emptyList()
                    val updatedData = data.copy(images = safeImages)
                    _memoFolderDetail.postValue(updatedData)
                    val imageIds = safeImages.map { it.imageId.toString() }
                    onFetched(imageIds)
                }
                .onFailure {
                    Log.e("MemoFolderViewModel", "폴더 상세 조회 실패: ${it.message}")
                    _memoFolderDetail.postValue(null)
                }
        }
    }

    fun searchMemoFolders(keyword: String) {
        viewModelScope.launch {
            memoRepository.searchMemos(keyword)
                .onSuccess { data ->
                    Log.d("MemoFolderViewModel", "검색 성공: ${data.data}")
                    _memoFolders.postValue(data.data.map { it.toMemoFolder() })
                }
                .onFailure { e ->
                    Log.e("MemoFolderViewModel", "검색 API 실패: ${e.message}")
                    _memoFolders.postValue(emptyList())
                }
        }
    }

    fun deleteMemoFolder(folderId: Long) {
        viewModelScope.launch {
            memoRepository.deleteMemoFolder(folderId)
                .onSuccess {
                    Log.d("MemoFolderViewModel", "폴더 삭제 성공: $folderId")
                    fetchMemoFolders() // ✅ 삭제 후 목록 새로고침
                }
                .onFailure { e ->
                    Log.e("MemoFolderViewModel", "폴더 삭제 실패: ${e.message}")
                }
        }
    }

        fun moveImages(folderId: Long, targetFolderId: String, imageIds: List<String>) {
            viewModelScope.launch {
                memoRepository.moveImages(folderId, targetFolderId, imageIds)
                    .onSuccess {
                        Log.d("MemoFolderViewModel", "사진 이동 성공")
                    }
                    .onFailure {
                        Log.e("MemoFolderViewModel", "사진 이동 실패: ${it.message}")
                    }
            }
        }

    fun deleteImages(folderId: String, imageIds: List<String>) {
        viewModelScope.launch {
            memoRepository.deleteImages(folderId, imageIds)
                .onSuccess { response ->
                    Log.d("MemoFolderViewModel", "사진 삭제 성공: $imageIds")
                    fetchMemoFolderDetails(folderId.toLong()) // ✅ 삭제 후 UI 갱신
                }
                .onFailure { error ->
                    Log.e("MemoFolderViewModel", "사진 삭제 실패: ${error.message}")
                }
        }
    }

    fun updateFolderName(folderId: String, newName: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            memoRepository.updateFolderName(folderId, newName)
                .onSuccess {
                    onSuccess()
                }
                .onFailure { error ->
                    onFailure(error.message ?: "폴더 이름 변경오류")
                }
        }
    }


    fun updateMemoText(folderId: String, newText: String) {
        viewModelScope.launch {
            memoRepository.updateMemoText(folderId, newText)
                .onSuccess {
                    Log.d("MemoFolderViewModel", "폴더 이름 수정 성공: $newText")
                    fetchMemoFolderDetails(folderId.toLong())
                }
                .onFailure {
                    Log.e("MemoFolderViewModel", "폴더 이름 수정 실패: ${it.message}")
                }
        }
    }

    }

