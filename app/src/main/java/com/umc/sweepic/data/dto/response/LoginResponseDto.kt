package com.umc.sweepic.data.dto.response

data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val email: String,
    val name: String
)