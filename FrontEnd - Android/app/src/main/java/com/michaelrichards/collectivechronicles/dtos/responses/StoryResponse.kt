package com.michaelrichards.collectivechronicles.dtos.responses

import java.time.LocalDateTime

data class StoryResponse(
    val storyId: Long,
    val title: String,
    val topic: String,
    val canon: List<StoryPieceResponse>,
    val maximumCanonSize: Int,
    val storyOwner: String,
    val isStoryOpen: Boolean,
    val storyStarted: LocalDateTime,
    val storyEdited: LocalDateTime,
    val isCallerOwner: Boolean
)
