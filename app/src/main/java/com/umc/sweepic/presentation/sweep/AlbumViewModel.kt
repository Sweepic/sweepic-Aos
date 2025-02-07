package com.umc.sweepic.presentation.sweep

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umc.sweepic.domain.model.sweep.AlbumList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val application: Application // Hilt를 통해 Application 주입
): ViewModel() {
    private val _albums = MutableLiveData<List<AlbumList>>()
    val albums: LiveData<List<AlbumList>> get() = _albums

    fun loadAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = application.applicationContext
            val albumList = mutableListOf<AlbumList>()
            val albumMap = mutableMapOf<String, AlbumList>() // 고유 앨범 저장용 Map

            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.RELATIVE_PATH // <-- 추가
            )
            val selection = "${MediaStore.Images.Media.MIME_TYPE} = ? OR ${MediaStore.Images.Media.MIME_TYPE} = ?"
            val selectionArgs = arrayOf("image/jpeg", "image/png")
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            cursor?.use {
                val bucketNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val bucketIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val imageIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val relPathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)

                while (it.moveToNext()) {
                    val albumName = it.getString(bucketNameColumn)
                    val albumId = it.getString(bucketIdColumn)
                    val imageId = it.getLong(imageIdColumn)
                    val albumUri = ContentUris.withAppendedId(uri, imageId).toString()
                    val relativePath = it.getString(relPathColumn)  // 대표 이미지의 경로

                    // 중복 체크: 고유 앨범 ID로 중복 제거
                    if (!albumMap.containsKey(albumId)) {
                        albumMap[albumId] = AlbumList(name = albumName, id = albumId, uri = albumUri, relativePath = relativePath)
                    }
                }
            }

            albumList.addAll(albumMap.values) // Map에 저장된 고유 앨범들을 리스트로 변환

            // UI 업데이트
            _albums.postValue(albumList)
        }
    }

}