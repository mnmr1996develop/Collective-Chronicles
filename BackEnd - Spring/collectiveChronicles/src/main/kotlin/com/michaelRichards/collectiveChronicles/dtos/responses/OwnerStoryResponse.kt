package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDateTime

data class OwnerStoryResponse(
    val storyId: Long,
    val title: String,
    val topic: String,
    val canon: List<StoryPieceResponse>,
    val potentialStoryPieces: List<StoryPieceResponse>,
    val storyOwner: String,
    val isStoryOpen: Boolean,
    val storyStarted: LocalDateTime,
    val storyEdited: LocalDateTime,
    val isCallerOwner: Boolean
)
