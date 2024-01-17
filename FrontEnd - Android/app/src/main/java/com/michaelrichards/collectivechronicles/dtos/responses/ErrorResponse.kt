package com.michaelrichards.collectivechronicles.dtos.responses

import java.time.LocalDateTime

data class ErrorResponse(
    val status: String,
    val message: String,
    val timeStamp: LocalDateTime
)
