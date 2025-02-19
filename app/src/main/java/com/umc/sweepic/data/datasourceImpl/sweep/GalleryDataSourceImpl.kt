package com.umc.sweepic.data.datasourceImpl.sweep

import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.umc.sweepic.data.datasource.GalleryDataSource
import com.umc.sweepic.domain.model.sweep.GalleryModel
import com.umc.sweepic.util.extension.getLatLongFromImage
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
            MediaStore.Images.Media.HEIGHT
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
                val actualId = cursor.getLong(idColumn)

                val uri = Uri.withAppendedPath(contentUri, actualId.toString())
                val (latitude, longitude) = getLatLongFromImage(uri, contentResolver) ?: Pair(0.0, 0.0)

                galleryImage.add(
                    GalleryModel(
                        id = actualId.toInt(),
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
                        latitude = latitude,
                        longitude = longitude
                    )
                )
                Log.d("GalleryDataSourceImpl", "dateTaken=$dateTaken, dateAdded=$dateAdded, finalDate=$finalDate, actualId=$actualId")

            }
        }

        return galleryImage

    }

    override fun fetchTrashedImages(limit: Int, offset: Int): List<GalleryModel> {
        val trashedImages = mutableListOf<GalleryModel>()

        // 휴지통 URI (Android 30 이상만 지원)
        val trashUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.IS_TRASHED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE
        )

        // Android 30 이상에서 IS_TRASHED를 필터링
        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            "${MediaStore.Images.Media.IS_TRASHED} = 1"
        } else {
            Log.e("GalleryDataSourceImpl", "IS_TRASHED is not supported on API < 30")
            return trashedImages
        }
        Log.d("GalleryDataSourceImpl", "Selection: $selection")

        val cursor = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d("GalleryDataSourceImpl", "Query Args: limit=$limit, offset=$offset")
                contentResolver.query(
                    trashUri,
                    projection,
                    Bundle().apply {
                        putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                        putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                        putStringArray(
                            ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            arrayOf(MediaStore.Images.Media.DATE_ADDED)
                        )
                        putInt(
                            ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                        )
                        putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                    },
                    null
                )
            } else {
                val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset"
                contentResolver.query(
                    trashUri,
                    projection,
                    selection,
                    null,
                    sortOrder
                )
            }
        } catch (e: Exception) {
            Log.e("GalleryDataSourceImpl", "Error querying trashed images", e)
            null
        }

        if (cursor == null) {
            Log.e("GalleryDataSourceImpl", "Cursor is null. Query failed.")
        } else {
            Log.d("GalleryDataSourceImpl", "Cursor count: ${cursor.count}")
            cursor.use { cur ->
                while (cur.moveToNext()) {
                    val id = cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val isTrashed = cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Images.Media.IS_TRASHED))
                    Log.d("GalleryDataSourceImpl", "ID: $id, IS_TRASHED: $isTrashed")
                    trashedImages.add(
                        GalleryModel(
                            id = id.toInt(),
                            uri = Uri.withAppendedPath(trashUri, id.toString()),
                            name = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                            fullName = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)),
                            mimeType = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)),
                            addedDate = cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)) * 1000,
                            folder = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)),
                            size = cur.getLong(cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                            width = cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)),
                            height = cur.getInt(cur.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)),
                            latitude = cur.getDouble(cur.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE)),
                            longitude = cur.getDouble(cur.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE))
                        )
                    )
                }
            }
        }

        Log.d("GalleryDataSourceImpl", "Fetched trashed images count: ${trashedImages.size}")
        return trashedImages
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun moveToTrash(uri: Uri): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_TRASHED, 1)
                }
                val rowsUpdated = contentResolver.update(uri, values, null, null)
                if (rowsUpdated > 0) {
                    Log.d("GalleryDataSourceImpl", "Successfully moved to trash: $uri")
                } else {
                    Log.e("GalleryDataSourceImpl", "Failed to move to trash: $uri")
                }
                rowsUpdated > 0
            } else {
                contentResolver.delete(uri, null, null) > 0
            }
        } catch (e: RecoverableSecurityException) {
            Log.e("GalleryDataSourceImpl", "RecoverableSecurityException: $e")
            false
        } catch (e: Exception) {
            Log.e("GalleryDataSourceImpl", "Error moving to trash: $e")
            false
        }
    }
}