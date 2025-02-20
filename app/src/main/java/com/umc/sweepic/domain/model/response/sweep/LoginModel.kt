package com.umc.sweepic.domain.model.response.login

data class LoginModel(
    val token: String,
    val userId: Int,
    val email: String,
    val name: String
)