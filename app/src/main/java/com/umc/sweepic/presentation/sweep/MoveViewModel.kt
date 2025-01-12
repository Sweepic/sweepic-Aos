package com.umc.sweepic.presentation.sweep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoveViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {
    val galleryPager = Pager(
        config = PagingConfig(pageSize = 50)
    ) {
        repository.getGalleryImagePagingSource()
    }.flow.cachedIn(viewModelScope)

}