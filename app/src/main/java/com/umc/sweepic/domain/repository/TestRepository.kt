package com.umc.sweepic.domain.repository

import com.umc.sweepic.data.dto.request.TestRequest
import com.umc.sweepic.domain.model.TestModel
import com.umc.sweepic.util.network.NetworkResult

interface TestRepository {
    suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel>
}