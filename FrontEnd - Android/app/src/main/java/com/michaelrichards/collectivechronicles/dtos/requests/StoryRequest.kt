package com.michaelrichards.collectivechronicles.dtos.requests

data class StoryRequest(
    val title: String,
    val topic: String,
    val isStoryOpen: Boolean,
    val maxCanonPieces: Int
)
