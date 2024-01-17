package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.requests.FullStoryRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.StoryPieceRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.FullStoryResponse
import com.michaelRichards.collectiveChronicles.models.FullStory
import com.michaelRichards.collectiveChronicles.models.StoryPiece
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.FullStoryRepository
import com.michaelRichards.collectiveChronicles.repositories.StoryPieceRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Transactional
class StoryService(
    private val userService: UserService,
    private val fullStoryRepository: FullStoryRepository,
    private val storyPieceRepository: StoryPieceRepository
) {

    fun findStoryById(id: Long): FullStory = fullStoryRepository.findById(id).orElseThrow { NotFoundException() }

    fun createStory(bearerToken: String, fullStoryRequest: FullStoryRequest): FullStoryResponse {
        val user = userService.findUserByBearerToken(bearerToken)
        val story = mapNewFullStoryRequestToFullStory(fullStoryRequest = fullStoryRequest, user = user)
        val savedStory = fullStoryRepository.save(story)
        return mapFullStoryToDto(savedStory)
    }

    private fun mapFullStoryToDto(fullStory: FullStory) = FullStoryResponse(
        storyId = fullStory.id!!,
        isStoryOpen = fullStory.isStoryOpen,
        storyEdited = fullStory.storyLastEdited!!,
        storyOwner = fullStory.storyOwner!!.username,
        storyStarted = fullStory.storyStarted!!,
        title = fullStory.title,
        topic = fullStory.topic
    )

    private fun mapNewFullStoryRequestToFullStory(fullStoryRequest: FullStoryRequest, user: User) = FullStory(
        title = fullStoryRequest.title.trim(),
        topic = fullStoryRequest.topic.trim(),
        storyOwner = user,
        isStoryOpen = fullStoryRequest.isStoryOpen,
        storyStarted = LocalDateTime.now(),
        storyLastEdited = LocalDateTime.now()
    )

    private fun mapNewStoryPieceRequestToFullStory(storyPieceRequest: StoryPieceRequest, user: User, fullStory: FullStory) = StoryPiece(
        text = storyPieceRequest.narrative,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        user = user,
        fullStory = fullStory
    )

    fun sendAddRequest(bearerToken: String, storyId: Long, storyPieceRequest: StoryPieceRequest){
        val fullStory = findStoryById(id = storyId)
        val user = userService.findUserByBearerToken(bearerToken)
        val storyPiece = mapNewStoryPieceRequestToFullStory(storyPieceRequest, user, fullStory)

        if (user == fullStory.storyOwner){
            fullStory.addToCanon(storyPiece)
        }else{
            fullStory.addPotentialPiece(storyPiece)
        }

        storyPieceRepository.save(storyPiece)
        fullStoryRepository.save(fullStory)

    }

}