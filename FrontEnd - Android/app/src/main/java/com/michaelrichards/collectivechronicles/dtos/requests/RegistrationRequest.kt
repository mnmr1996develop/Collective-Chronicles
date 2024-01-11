package com.michaelrichards.collectivechronicles.dtos.requests

import java.time.LocalDate

data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val birthday: LocalDate,
    val password: String
)
