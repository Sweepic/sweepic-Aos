package com.umc.sweepic.data.datasource.challenge

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeDeleteRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateChallengeUpdateRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationChallengeRequestDto
import com.umc.sweepic.data.dto.request.challenge.CreateLocationLogicTestRequestDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeDeleteResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateChallengeUpdateResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationChallengeResponseDto
import com.umc.sweepic.data.dto.response.challenge.CreateLocationLogicTestResponseDto

interface ChallengeDataSource {
    suspend fun fetchChallengeUpdate(request: CreateChallengeUpdateRequestDto): BaseResponse<CreateChallengeUpdateResponseDto>
    suspend fun fetchChallengeDelete(request: CreateChallengeDeleteRequestDto): BaseResponse<CreateChallengeDeleteResponseDto>

    suspend fun fetchChallengeLocationLogicTestChallengeCreate(request: List<CreateLocationLogicTestRequestDto>): BaseResponse<List<CreateLocationLogicTestResponseDto>>
    suspend fun fetchChallengeLocationChallengeCreate(request: CreateLocationChallengeRequestDto): BaseResponse<CreateLocationChallengeResponseDto>

}
