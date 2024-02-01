package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDate
import java.time.LocalDateTime

data class UserDetailsDTO(
    val firstName: String,
    val lastName: String,
    val username: String,
    val birthday: LocalDate,
    val accountCreated: LocalDateTime
)
