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
                    _memoFolderDetail.postValue(data)
                }
                .onFailure {
                    _memoFolderDetail.postValue(null)
                }
        }
    }
}