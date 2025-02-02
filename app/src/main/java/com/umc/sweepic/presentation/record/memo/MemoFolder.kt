package com.umc.sweepic.presentation.record.memo
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoFolder(
    val id: Int, //폴더 고유 아이디
    val title: String, //메모 폴더 제목
    val date: String, //메모 날짜
    val content: String?,         // 메모 내용
    val imageResIds: List<Int>    // 이미지 리스트
) : Parcelable {
    @IgnoredOnParcel
    val photoCount: Int
        get() = imageResIds.size  // 사진 수 = 이미지 리스트 수로 가져오기
}

