package com.umc.sweepic.domain.repository.sweep

import androidx.paging.PagingSource
import com.umc.sweepic.domain.model.sweep.Gallery

interface GalleryRepository {
    fun getGalleryImagePagingSource(): PagingSource<Int, Gallery>
    fun getAllGalleryImagesDesc(): List<Gallery>
}