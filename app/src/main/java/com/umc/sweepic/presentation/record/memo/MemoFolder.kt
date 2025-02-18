package com.umc.sweepic.presentation.record.memo

import android.os.Parcelable
import com.umc.sweepic.domain.model.response.sweep.RecordMemoListModel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class MemoFolder(
    val id: Int,
    val title: String,
    val date: String,
    val content: String?,
    val imageUrl: String?,
    val imageCount: Int
) : Parcelable {
    companion object {

        fun formatDate(isoDate: String?): String {
            if (isoDate.isNullOrEmpty()) return ""
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val date = inputFormat.parse(isoDate)
                outputFormat.format(date ?: return isoDate)
            } catch (e: Exception) {
                isoDate // 변환 실패 시 원본 반환
            }
        }

        fun RecordMemoListModel.MemoFolderModel.toMemoFolder(): MemoFolder {
            return MemoFolder(
                id = folderId.toInt(),
                title = folderName,
                date = createdAt ?: "",
                content = imageText?.takeIf{it.isNotEmpty()},
                imageUrl = firstImageUrl?.takeIf { it.isNotEmpty() },
                imageCount = imageCount
            )
        }
    }
}
