package com.umc.sweepic.data.datasource

import android.net.Uri
import com.umc.sweepic.domain.model.sweep.GalleryModel

interface GalleryDataSource {
    fun fetchGalleryImages(limit: Int, offset: Int): List<GalleryModel>
    fun fetchTrashedImages(limit: Int, offset: Int): List<GalleryModel>
    fun moveToTrash(uri: Uri): Boolean
}