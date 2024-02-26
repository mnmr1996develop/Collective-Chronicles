package com.michaelrichards.collectivechronicles.dtos.responses

import java.time.LocalDateTime

data class OwnerStoryResponse(
    val storyResponse: StoryResponse,
    val potentialStoryPieces: List<StoryPieceResponse>,
)
