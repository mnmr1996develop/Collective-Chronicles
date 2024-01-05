package com.michaelRichards.collectiveChronicles.dtos.requests

import java.time.LocalDate

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val password: String,
    val birthday: LocalDate
)
