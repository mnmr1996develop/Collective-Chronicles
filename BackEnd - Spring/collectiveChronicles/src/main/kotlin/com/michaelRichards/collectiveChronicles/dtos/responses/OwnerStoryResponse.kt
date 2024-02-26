package com.michaelRichards.collectiveChronicles.dtos.responses

data class OwnerStoryResponse(
    val storyResponse: StoryResponse,
    val potentialStoryPieces: List<StoryPieceResponse>,
)
