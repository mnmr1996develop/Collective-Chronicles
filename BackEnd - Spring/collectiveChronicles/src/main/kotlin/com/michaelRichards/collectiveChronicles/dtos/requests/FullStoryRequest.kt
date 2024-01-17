package com.michaelRichards.collectiveChronicles.dtos.requests

data class FullStoryRequest(
    val title: String,
    val topic: String,
    val isStoryOpen: Boolean
)
