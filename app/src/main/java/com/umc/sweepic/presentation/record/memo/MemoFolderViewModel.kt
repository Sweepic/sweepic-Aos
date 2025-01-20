package com.umc.sweepic.presentation.record.memo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.sweepic.R

class MemoFolderViewModel : ViewModel() {
    private val _memoFolders = MutableLiveData<List<MemoFolder>>()
    val memoFolders: LiveData<List<MemoFolder>> get() = _memoFolders

    init {
        _memoFolders.value = listOf(
            MemoFolder(1, "쇼핑", "2024.12.22", "메모 내용내용내용", listOf()),
            MemoFolder(2, "글귀", "2024.12.22", null, listOf(R.drawable.img_record_ex)),
            MemoFolder(3, "짤/밈", "2024.12.22", "메모 내용내용", listOf(R.drawable.img_record_ex, R.drawable.img_memo_imagelist)),
            MemoFolder(4,"트렌드","2024.12.22","메모 내용내용",listOf(R.drawable.img_memo_imagelist, R.drawable.img_record_ex)),
            MemoFolder(5,"기타","2024.12.22",null, listOf())
        )
    }
}