package com.umc.sweepic.data.datasourceImpl.sweep

import com.umc.sweepic.data.datasource.MypageDataSource
import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.response.mypage.GetUserInformationResponseDto
import com.umc.sweepic.data.service.MypageService
import retrofit2.Response
import javax.inject.Inject

class MypageDataSourceImpl @Inject constructor(
    private val mypageService: MypageService
) : MypageDataSource {

    override suspend fun getUserInformation(): BaseResponse<GetUserInformationResponseDto> =
        mypageService.getUserInformation()

    override suspend fun withdrawal(): BaseResponse<Unit> =
        mypageService.withdrawal()

    override suspend fun logoutUser(): Response<Unit> =
        mypageService.logoutUser()
}
