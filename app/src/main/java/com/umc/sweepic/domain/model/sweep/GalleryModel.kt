package com.umc.sweepic.domain.model.sweep

import android.net.Uri
import java.util.Date

data class GalleryModel(
    val id: Int,
    val uri: Uri,
    val name: String,
    val fullName: String,
    val mimeType: String,
    val addedDate: Long,
    val folder: String,
    val size: Long,
    val width: Int,
    val height: Int,
){
    fun toGallery() = Gallery(
        id,
        uri,
        name,
        fullName,
        mimeType,
        Date(addedDate),
        folder,
        size,
        width,
        height,
    )

}


data class Gallery(
    val id: Int,
    val uri: Uri,
    val name: String,
    val fullName: String,
    val mimeType: String,
    val addedDate: Date,
    val folder: String,
    val size: Long,
    val width: Int,
    val height: Int,
)