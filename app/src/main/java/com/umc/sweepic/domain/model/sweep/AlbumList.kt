package com.umc.sweepic.domain.model.sweep

data class AlbumList(
    val name: String,
    val id: String, // 앨범 ID
    val uri: String,
    val relativePath: String? = null  // 경로 정보
)
