package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDateTime

data class FullStoryResponse(
    val storyId: Long,
    val title: String,
    val topic: String,
    val storyOwner: String,
    val isStoryOpen: Boolean,
    val storyStarted: LocalDateTime,
    val storyEdited: LocalDateTime
)
