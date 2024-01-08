package com.michaelRichards.collectiveChronicles.dtos.responses

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)
