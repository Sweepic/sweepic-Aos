package com.umc.sweepic.presentation.record.history.award

data class ItemHistoryLastBest(
    val date: String,       // 예: "2024년 11월"
    val photoUrls: List<String> // 나중에 API에서 URL을 받을 경우 String 리스트
)
