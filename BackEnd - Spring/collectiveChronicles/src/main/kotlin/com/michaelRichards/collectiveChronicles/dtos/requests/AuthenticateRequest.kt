package com.michaelRichards.collectiveChronicles.dtos.requests

data class AuthenticateRequest(
    val username: String,
    val password: String
)
