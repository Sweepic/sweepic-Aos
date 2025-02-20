package com.umc.sweepic.data.datasource

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.mypage.GetUserInformationResponseDto
import retrofit2.Response

interface MypageDataSource {
    suspend fun getUserInformation(): BaseResponse<GetUserInformationResponseDto>
    suspend fun withdrawal(): BaseResponse<Unit>
    suspend fun logoutUser(): Response<Unit>
}
