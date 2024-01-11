package com.michaelrichards.collectivechronicles.dtos.responses

import java.time.LocalDateTime

data class ErrorResponse(
    val status: String,
    val message: String,
    val reason: String,
    val timeStamp: LocalDateTime
)
