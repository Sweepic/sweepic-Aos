package com.umc.sweepic.presentation.sweep

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.response.sweep.SweepMemoListModel
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SweepViewModel @Inject constructor(
    private val repository: SweepRepository
): ViewModel() {
    private val _folderList = MutableLiveData<List<SweepMemoListModel.MemoFolderModel>>()
    val folderList: LiveData<List<SweepMemoListModel.MemoFolderModel>> = _folderList

    fun fetchFolderList() {
        viewModelScope.launch {
            repository.fetchSweepMemoList().onSuccess { memoList ->
                _folderList.value = memoList.data
                Log.d("MemoList", memoList.toString())
            }.onFailure { exception ->
                Log.e("MemoList", exception.stackTraceToString())
            }
        }
    }
}