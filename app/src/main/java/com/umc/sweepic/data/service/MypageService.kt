package com.umc.sweepic.data.service

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.mypage.GetUserInformationResponseDto
import retrofit2.http.GET
import retrofit2.http.PATCH

interface MypageService {
    @GET("user/mypage")
    suspend fun getUserInformation(): BaseResponse<GetUserInformationResponseDto>

    @PATCH("user/mypage")
    suspend fun withdrawal(): BaseResponse<Unit>

    @GET("user/mypage/logout")
    suspend fun logoutUser(): BaseResponse<Unit>
}
