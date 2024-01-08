package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDate
import java.time.LocalDateTime

data class AdminDetailResponse(
    val firstName: String,
    val lastName: String,
    val username: String,
    val birthday: LocalDate,
    val accountCreatedAt: LocalDateTime
)