package com.umc.sweepic.data.repositoryImpl

import com.umc.sweepic.data.dto.BaseResponse
import com.umc.sweepic.data.dto.request.TestRequest
import com.umc.sweepic.data.dto.response.TestResponse
import com.umc.sweepic.data.service.TestService
import com.umc.sweepic.domain.model.TestModel
import com.umc.sweepic.domain.repository.TestRepository
import com.umc.sweepic.util.network.NetworkResult
import com.umc.sweepic.util.network.handleApi
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testService: TestService
) : TestRepository {
    override suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel> {
        return handleApi({testService.fetchTest(request)}) {response: BaseResponse<TestResponse> -> response.success.toTestModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
}