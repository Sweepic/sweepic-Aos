package com.umc.sweepic.data.datasource

import com.umc.sweepic.domain.model.sweep.GalleryModel

interface GalleryDataSource {
    fun fetchGalleryImages(limit: Int, offset: Int): List<GalleryModel>
}