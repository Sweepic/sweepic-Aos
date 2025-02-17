package com.umc.sweepic.presentation.challenge.adapter

import com.umc.sweepic.domain.model.response.challenge.GetUserChallengeResponseModel

data class Challenge(
    val id: String,
    val title: String,
    val context: String,
    val challengeLocation: String?,  // 위치 기반 챌린지
    val challengeDate: String?,  // 날짜 기반 챌린지
    val requiredCount: Int,  // 목표 장수
    val remainingCount: Int,  // 남은 장수
    val userId: String,
    val createdAt: String,
    val updatedAt: String,
    val acceptedAt: String?,
    val completedAt: String?,
    val status: Int,  // 1 = 새로운 챌린지, 2 = 진행 중, 3 = 완료됨
    val photos: List<String> // 사진 목록 추가
) {
    companion object {
        fun fromResponse(response: GetUserChallengeResponseModel): Challenge {
            return Challenge(
                id = response.id,
                title = response.title,
                context = response.context,
                challengeLocation = response.challengeLocation,
                challengeDate = response.challengeDate,
                requiredCount = response.requiredCount,
                remainingCount = response.remainingCount,
                userId = response.userId,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
                acceptedAt = response.acceptedAt,
                completedAt = response.completedAt,
                status = response.status,
                photos = emptyList() // 필요하면 추가
            )
        }

        fun List<GetUserChallengeResponseModel>.toChallengeList(): List<Challenge> {
            return this.map { fromResponse(it) }
        }
    }
}
