package com.umc.sweepic.domain.repository.sweep

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.paging.PagingSource
import com.umc.sweepic.domain.model.sweep.Gallery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface GalleryRepository {
    fun getGalleryImagePagingSource(): PagingSource<Int, Gallery>
}