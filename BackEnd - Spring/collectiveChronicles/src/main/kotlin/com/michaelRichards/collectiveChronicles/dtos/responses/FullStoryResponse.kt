package com.michaelRichards.collectiveChronicles.dtos.responses

import com.michaelRichards.collectiveChronicles.models.StoryPiece
import java.time.LocalDateTime

data class FullStoryResponse(
    val storyId: Long,
    val title: String,
    val topic: String,
    val canon: List<StoryPieceResponse>,
    val storyOwner: String,
    val isStoryOpen: Boolean,
    val storyStarted: LocalDateTime,
    val storyEdited: LocalDateTime,
    val isCallerOwner: Boolean
)
