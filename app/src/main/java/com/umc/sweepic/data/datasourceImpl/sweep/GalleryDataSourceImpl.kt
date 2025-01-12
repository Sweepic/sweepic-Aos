package com.umc.sweepic.data.datasourceImpl.sweep

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.umc.sweepic.data.datasource.GalleryDataSource
import com.umc.sweepic.domain.model.sweep.GalleryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context
) : GalleryDataSource {
    private val contentResolver = context.contentResolver

    override fun fetchGalleryImages(limit: Int, offset: Int): List<GalleryModel> {
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        )
        val galleryImage = mutableListOf<GalleryModel>()
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.query(
                contentUri,
                projection,
                Bundle().apply {
                    // Limit & Offset
                    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                    // Sort function
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(MediaStore.Images.Media.DATE_ADDED)
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )

                    // Selection
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                }, null
            )
        } else {
            val sortOrder =
                "${MediaStore.Images.Media.DATE_TAKEN} DESC LIMIT $limit OFFSET $offset"
            contentResolver.query(
                contentUri,
                projection,
                selection,
                null,
                sortOrder
            )
        }?.use { cursor ->
            while (cursor.moveToNext()) {
                galleryImage.add(
                    GalleryModel(
                        uri = Uri.withAppendedPath(
                            contentUri,
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)).toString()
                        ),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)),
                        fullName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                        mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)),
                        addedDate = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)),
                        folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                        size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                        width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)),
                        height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)),
                    )
                )
            }
        }

        return galleryImage
    }
}