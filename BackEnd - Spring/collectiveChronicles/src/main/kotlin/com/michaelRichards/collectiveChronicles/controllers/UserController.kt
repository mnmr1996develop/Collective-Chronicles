package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.responses.OwnerStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.StoryPieceResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.UserDetailsDTO
import com.michaelRichards.collectiveChronicles.services.StoryService
import com.michaelRichards.collectiveChronicles.services.UserService
import com.michaelRichards.collectiveChronicles.utils.Variables
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Variables.V1_USER_CONTROLLER_PATH)
class UserController(
    private val userService: UserService,
    private val storyService: StoryService
) {

    @GetMapping
    fun getUserDetails(): ResponseEntity<List<UserDetailsDTO>> =
        ResponseEntity.ok(userService.getAllUserDetails())

    @DeleteMapping
    fun deleteAccount(
        @RequestHeader(Variables.AUTHORIZATION) jwtToken: String
    ): ResponseEntity<Boolean> {
        userService.deleteUserByToken(jwtToken)
        return ResponseEntity.ok(true)
    }

    @GetMapping("myStories")
    fun getMyStories(
        @RequestHeader(Variables.AUTHORIZATION) jwtToken: String,
        @RequestParam pageNumber: Int? = null,
        @RequestParam pageSize: Int? = null,
        @RequestParam isAscending: Boolean? = null
    ): ResponseEntity<List<OwnerStoryResponse>> =
        ResponseEntity.ok().body(storyService.getUserStories(jwtToken, pageNumber, pageSize, isAscending ))


    @GetMapping("myStoryPieces")
    fun getMyStoryPieces(
        @RequestHeader(Variables.AUTHORIZATION) jwtToken: String,
        ): ResponseEntity<List<StoryPieceResponse>> = ResponseEntity.ok().body(storyService.getUserStoryPieces(jwtToken))

}