package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.requests.FullStoryRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.StoryPieceRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.OwnerStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.StoryResponse
import com.michaelRichards.collectiveChronicles.services.StoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    ): ResponseEntity<StoryResponse> =
        ResponseEntity.created(URI(BASE_PATH)).body(storyService.createStory(bearerToken, fullStoryRequest))


    @GetMapping("{storyId}")
    fun getFullStory(
        @RequestHeader(AUTHORIZATION) bearerToken: String,
        @PathVariable("storyId") storyId: Long
    ): ResponseEntity<StoryResponse> =
        ResponseEntity.ok(storyService.getPublicFullStory(bearerToken,storyId))


    @GetMapping("{storyId}/fullStoryData")
    fun getFullStoryData(
        @RequestHeader(AUTHORIZATION) bearerToken: String,
        @PathVariable("storyId") storyId: Long
    ): ResponseEntity<OwnerStoryResponse> =
        ResponseEntity.ok(storyService.getOwnerFullStory(bearerToken,storyId))

    @PostMapping("{storyId}/swap")
    fun swapStoryPieces(
        @RequestHeader(AUTHORIZATION) bearerToken: String,
        @PathVariable("storyId") storyId: Long,
        @RequestParam("storyPiecePlace1") storyPiecePlace1: Int,
        @RequestParam("storyPiecePlace2") storyPiecePlace2: Int
    ): ResponseEntity<StoryResponse> =
        ResponseEntity.ok(storyService.swapStoryOrder(bearerToken,storyId, storyPiecePlace1, storyPiecePlace2))

    @DeleteMapping("{storyId}")
    fun deleteStory(
        @RequestHeader(AUTHORIZATION) jwtToken: String,
        @PathVariable("storyId") storyId: Long,
        @RequestBody storyPieceRequest: StoryPieceRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(storyService.deleteStory(bearerToken = jwtToken, storyId = storyId))

    @PostMapping("{storyId}/addRequest")
    fun addRequest(
        @RequestHeader(AUTHORIZATION) jwtToken: String,
        @PathVariable("storyId") storyId: Long,
        @RequestBody storyPieceRequest: StoryPieceRequest
    ): ResponseEntity<Unit> =
        ResponseEntity.ok(storyService.sendAddRequest(bearerToken = jwtToken, storyId = storyId, storyPieceRequest = storyPieceRequest))



    @DeleteMapping("{storyId}/storyPieces/{pieceId}")
    fun deleteRequest(
        @RequestHeader(AUTHORIZATION) jwtToken: String,
        @PathVariable("storyId") storyId: Long,
        @PathVariable("pieceId") pieceId: Long
    ): ResponseEntity<Unit> = ResponseEntity.ok().body(storyService.deleteStoryPieceRequest(jwtToken, storyId, pieceId))
}