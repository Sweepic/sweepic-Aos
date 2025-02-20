package com.umc.sweepic.util.extension

import android.content.ContentResolver
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.IOException

fun getLatLongFromImage(uri: Uri, contentResolver: ContentResolver): Pair<Double, Double> {
    return try {
        contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            val exif = ExifInterface(parcelFileDescriptor.fileDescriptor)
            val latLong = FloatArray(2)
            if (exif.getLatLong(latLong)) {
                Pair(latLong[0].toDouble(), latLong[1].toDouble())
            } else {
                Log.d("EXIF", "GPS 데이터가 없습니다.")
                Pair(0.0, 0.0)  // 위치 정보가 없으면 기본값 반환
            }
        } ?: Pair(0.0, 0.0)
    } catch (e: IOException) {
        e.printStackTrace()
        Pair(0.0, 0.0)
    }
}

