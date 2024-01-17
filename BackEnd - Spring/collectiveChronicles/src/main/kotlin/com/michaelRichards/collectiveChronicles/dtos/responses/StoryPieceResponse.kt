package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDateTime

data class StoryPieceResponse(
    val pieceId: Long,
    val narrative: String,
    val createdAt: LocalDateTime,
    val lastUpdatedAt: LocalDateTime,
    val owner: String,
    val isCallerOwner: Boolean
)