package com.michaelRichards.collectiveChronicles.dtos.responses

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class APIException(
    val status: HttpStatus,
    val message: String,
    val timeStamp: LocalDateTime
)
