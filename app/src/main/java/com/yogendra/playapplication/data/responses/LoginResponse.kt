package com.yogendra.playapplication.data.responses

data class LoginResponseSuccess(
    val token: String
)

data class LoginResponseFailure(
    val error: String,
    val description: String
)