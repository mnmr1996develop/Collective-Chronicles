package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDateTime

data class PublicStoryResponse(
    val storyId: Long,
    val title: String,
    val topic: String,
    val canon: List<StoryPieceResponse>,
    val storyOwner: String,
    val isStoryOpen: Boolean,
    val storyStarted: LocalDateTime,
    val storyLastEdited: LocalDateTime,
    val isCallerOwner: Boolean
)
