package com.umc.sweepic.presentation.record.memo

import android.os.Parcelable
import com.umc.sweepic.domain.model.RecordMemoListModel
import kotlinx.parcelize.Parcelize

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
