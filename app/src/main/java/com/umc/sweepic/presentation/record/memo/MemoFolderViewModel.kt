package com.umc.sweepic.presentation.record.memo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.MemoFolderDetailModel
import com.umc.sweepic.domain.model.RecordMemoListModel
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

    // ✅ 메모 목록 가져오기
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


    fun fetchMemoFolderDetails(folderId: Long) {
        viewModelScope.launch {
            memoRepository.fetchMemoFolderDetails(folderId)
                .onSuccess { data ->
                    Log.d("MemoFolderViewModel", "폴더 상세 조회 성공: $data")
                    _memoFolderDetail.postValue(data)
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


}