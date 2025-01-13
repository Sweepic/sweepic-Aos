package com.umc.sweepic.data.datasourceImpl.sweep

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
            MediaStore.Images.Media.DATE_ADDED, // ← 추가
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
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

            while (cursor.moveToNext()) {
                val dateTaken = cursor.getLong(dateTakenColumn)   // 밀리초
                val dateAdded = cursor.getLong(dateAddedColumn)   // 초
                val finalDate = if (dateTaken > 0) {
                    dateTaken
                } else {
                    dateAdded * 1000  // 초 → 밀리초로 보정
                }

                galleryImage.add(
                    GalleryModel(
                        uri = Uri.withAppendedPath(
                            contentUri,
                            cursor.getLong(idColumn).toString()
                        ),
                        name = cursor.getString(titleColumn),
                        fullName = cursor.getString(displayNameColumn),
                        mimeType = cursor.getString(mimeTypeColumn),
                        // addedDate 대신, 최종 결정된 finalDate 사용
                        addedDate = finalDate,
                        folder = cursor.getString(folderColumn),
                        size = cursor.getLong(sizeColumn),
                        width = cursor.getInt(widthColumn),
                        height = cursor.getInt(heightColumn),
                    )
                )
                Log.d("GalleryDataSourceImpl", "dateTaken=$dateTaken, dateAdded=$dateAdded, finalDate=$finalDate")

            }
        }

        return galleryImage
    }
}