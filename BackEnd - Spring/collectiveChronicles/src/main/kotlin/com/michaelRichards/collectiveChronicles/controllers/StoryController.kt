package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.requests.FullStoryRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.StoryPieceRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.FullStoryResponse
import com.michaelRichards.collectiveChronicles.services.StoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private const val BASE_PATH = "/api/v1/story"
private const val AUTHORIZATION = "Authorization"

@RestController
@RequestMapping(BASE_PATH)
class StoryController(
    private val storyService: StoryService
) {

    @PostMapping
    fun startStory(
        @RequestHeader(AUTHORIZATION) bearerToken: String,
        @RequestBody fullStoryRequest: FullStoryRequest
    ): ResponseEntity<FullStoryResponse> =
        ResponseEntity.created(URI(BASE_PATH)).body(storyService.createStory(bearerToken, fullStoryRequest))

    @PostMapping("{storyId}/addRequest")
    fun addRequest(
        @RequestHeader(AUTHORIZATION) jwtToken: String,
        @PathVariable("storyId") storyId: Long,
        @RequestBody storyPieceRequest: StoryPieceRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(storyService.sendAddRequest(bearerToken = jwtToken, storyId = storyId, storyPieceRequest = storyPieceRequest))
}