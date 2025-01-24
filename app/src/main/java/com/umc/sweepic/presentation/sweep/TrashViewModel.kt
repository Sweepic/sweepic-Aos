package com.umc.sweepic.presentation.sweep

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val repository: GalleryRepository
): ViewModel() {
    val pagedTrashedImages: Flow<PagingData<Gallery>> = Pager(
        config = PagingConfig(
            pageSize = 20, // 한 페이지에 로드할 항목 수
            enablePlaceholders = false // 자리 표시자 사용 여부 (false: 사용 안 함)
        ),
        pagingSourceFactory = {
            // GalleryRepository에서 제공하는 PagingSource로 휴지통 데이터 로드
            repository.getTrashedImagesPagingSource()
        }
    ).flow.cachedIn(viewModelScope) // ViewModel 범위 내에서 데이터 캐싱

    fun moveImageToTrash(uri: Uri): Boolean {
        return repository.moveToTrash(uri)
    }

    val trashPager = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            repository.getTrashedImagesPagingSource()
        }
    ).flow.cachedIn(viewModelScope)

}