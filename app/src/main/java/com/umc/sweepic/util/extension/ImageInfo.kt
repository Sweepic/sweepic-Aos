package com.umc.sweepic.util.extension

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.Q)
fun getLatLongFromImage(uri: Uri, context: Context): Pair<Double, Double> {
    return try {
        val contentResolver = context.contentResolver

        // 원본 URI 가져오기 (ACCESS_MEDIA_LOCATION 권한 필요)
        val finalUri = try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_MEDIA_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                MediaStore.setRequireOriginal(uri)
            } else {
                Log.d("EXIF", "ACCESS_MEDIA_LOCATION 권한이 없습니다.")
                uri
            }
        } catch (e: IllegalArgumentException) {
            Log.e("EXIF", "원본 URI 요청 실패: ${e.message}")
            uri // 기본 URI 사용
        }

        contentResolver.openInputStream(finalUri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            val latLong = FloatArray(2)

            if (exif.getLatLong(latLong)) {
                return Pair(latLong[0].toDouble(), latLong[1].toDouble())
            }

            // 🔹 getLatLong()이 실패한 경우 수동으로 GPS 태그 읽기
            val lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
            val lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val lonRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

            if (lat != null && lon != null && latRef != null && lonRef != null) {
                val latitude = convertToDegrees(lat) * if (latRef == "N") 1 else -1
                val longitude = convertToDegrees(lon) * if (lonRef == "E") 1 else -1
                return Pair(latitude, longitude)
            } else {
                Log.d("EXIF", "GPS 데이터가 없습니다.")
                return Pair(0.0, 0.0)
            }
        } ?: Pair(0.0, 0.0)
    } catch (e: IOException) {
        Log.e("EXIF", "파일 읽기 실패: ${e.message}")
        Pair(0.0, 0.0)
    }
}

// 🔹 DMS(Degrees, Minutes, Seconds) 데이터를 변환하는 함수
private fun convertToDegrees(dms: String): Double {
    val parts = dms.split(",", "/").map { it.trim().toDoubleOrNull() ?: 0.0 }
    return if (parts.size >= 6) {
        parts[0] / parts[1] + (parts[2] / parts[3]) / 60.0 + (parts[4] / parts[5]) / 3600.0
    } else {
        0.0
    }
}
