package com.umc.sweepic.data.dto.response

import com.umc.sweepic.domain.model.TestModel

data class TestResponse (
    val body: String
){
    fun toTestModel() = TestModel(body)
}