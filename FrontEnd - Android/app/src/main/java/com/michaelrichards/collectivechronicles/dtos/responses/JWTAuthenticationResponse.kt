package com.michaelrichards.collectivechronicles.dtos.responses

data class JWTAuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)
