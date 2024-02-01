package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.requests.FullStoryRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.StoryPieceRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.OwnerStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.PublicStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.StoryPieceResponse
import com.michaelRichards.collectiveChronicles.exceptions.authorizationExceptions.AuthorizationExceptions
import com.michaelRichards.collectiveChronicles.exceptions.storyExceptions.StoryExceptions
import com.michaelRichards.collectiveChronicles.models.FullStory
import com.michaelRichards.collectiveChronicles.models.StoryPiece
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.FullStoryRepository
import com.michaelRichards.collectiveChronicles.repositories.StoryPieceRepository
import com.michaelRichards.collectiveChronicles.utils.Variables
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.max

@Service
@Transactional
class StoryService(
    private val userService: UserService,
    private val fullStoryRepository: FullStoryRepository,
    private val storyPieceRepository: StoryPieceRepository
) {

    fun findStoryById(id: Long): FullStory =
        fullStoryRepository.findById(id).orElseThrow { StoryExceptions.NotFoundException("$id not found") }

    fun findPieceById(id: Long): StoryPiece =
        storyPieceRepository.findById(id).orElseThrow { StoryExceptions.NotFoundException("$id not found") }

    fun getPublicFullStory(bearerToken: String, storyId: Long): PublicStoryResponse {
        val story = findStoryById(storyId)
        val user = userService.findUserByBearerToken(bearerToken)

        return mapPublicStoryToDto(story, user)
    }

    fun getOwnerFullStory(bearerToken: String, storyId: Long): OwnerStoryResponse {
        val story = findStoryById(storyId)
        val user = userService.findUserByBearerToken(bearerToken)
        return mapOwnerStoryToDTO(story, user)
    }

    fun getUserStories(
        jwtToken: String,
        pageNumber: Int? = null,
        pageSize: Int? = null,
        isAscending: Boolean?
    ): List<OwnerStoryResponse> {
        val user = userService.findUserByBearerToken(jwtToken)

        val stories =
            if (pageNumber == null || pageSize == null) fullStoryRepository.findByStoryOwnerOrderByLastEditedDesc(
                user
            ) else {
                if (pageNumber < 0) throw StoryExceptions.IndexOutOfBound(pageNumber, Variables.PAGE_NUMBER)
                if (pageSize < 1 || pageSize > 100) throw StoryExceptions.IndexOutOfBound(pageSize, Variables.PAGE_SIZE)

                val pageable: Pageable =
                    if (isAscending == null)
                        PageRequest.of(pageNumber, pageSize)
                    else
                        PageRequest.of(
                            pageNumber, pageSize,
                            if (isAscending) Sort.by(Variables.LAST_EDITED)
                                .ascending() else Sort.by(Variables.LAST_EDITED).descending()
                        )
                fullStoryRepository.findByStoryOwner(user, pageable)
            }
        return stories.map { fullStory: FullStory -> mapOwnerStoryToDTO(fullStory, user) }
    }

    fun getUserStoryPieces(jwtToken: String): List<StoryPieceResponse> {
        val user = userService.findUserByBearerToken(jwtToken)
        val storyPieces = storyPieceRepository.findByUserOrderByCreatedAtDesc(user)
        return storyPieces.map { storyPiece: StoryPiece -> mapStoryPieceToDto(storyPiece, user) }
    }

    fun swapStoryOrder(bearerToken: String, storyId: Long, pieceId1: Int, pieceId2: Int): PublicStoryResponse {
        val story = findStoryById(storyId)
        val user = userService.findUserByBearerToken(bearerToken)

        if (pieceId1 < story.canon.size && pieceId2 < story.canon.size) {
            story.canon.swap(pieceId1, pieceId2)
            fullStoryRepository.save(story)
        } else {
            val variableName = if (pieceId1 > pieceId2) "pieceId1" else "pieceId2"
            throw StoryExceptions.IndexOutOfBound(index = max(pieceId1, pieceId2), variableName)
        }
        /* if (story.storyOwner != user){
             throw Exception()
         }

         print(story.canon.size)
         print(pieceId1)
         print(pieceId2)

         if (story.canon.size < pieceId1 && story.canon.size < pieceId2){
             story.canon.swap(pieceId1, pieceId2)
             fullStoryRepository.save(story)
         } else throw IndexOutOfBoundsException()*/

        return mapPublicStoryToDto(story, user)
    }

    fun createStory(bearerToken: String, fullStoryRequest: FullStoryRequest): PublicStoryResponse {
        val user = userService.findUserByBearerToken(bearerToken)
        val story = mapNewFullStoryRequestToFullStory(fullStoryRequest = fullStoryRequest, user = user)
        user.ownedStories.add(story)
        val savedStory = fullStoryRepository.save(story)
        return mapPublicStoryToDto(savedStory, user)
    }

    fun deleteStory(bearerToken: String, storyId: Long) {
        val fullStory = findStoryById(id = storyId)
        val user = userService.findUserByBearerToken(bearerToken)
        if (user == fullStory.storyOwner) {
            fullStoryRepository.delete(fullStory)
        }

    }

    private fun mapPublicStoryToDto(fullStory: FullStory, user: User) = PublicStoryResponse(
        storyId = fullStory.id!!,
        isStoryOpen = fullStory.isStoryOpen,
        storyLastEdited = fullStory.lastEdited!!,
        storyOwner = fullStory.storyOwner!!.username,
        canon = fullStory.canon.map { storyPiece -> mapStoryPieceToDto(storyPiece, user) },
        storyStarted = fullStory.created!!,
        title = fullStory.title,
        topic = fullStory.topic,
        isCallerOwner = user == fullStory.storyOwner
    )

    private fun mapOwnerStoryToDTO(fullStory: FullStory, user: User): OwnerStoryResponse {
        if (fullStory.storyOwner != user) throw AuthorizationExceptions.UnAuthorizedAction("Unauthorized User Action")

        return OwnerStoryResponse(
            storyId = fullStory.id!!,
            title = fullStory.title,
            topic = fullStory.topic,
            canon = fullStory.canon.map { storyPiece: StoryPiece -> mapStoryPieceToDto(storyPiece, user) },
            potentialStoryPieces = fullStory.potentialPieces.map { storyPiece: StoryPiece ->
                mapStoryPieceToDto(
                    storyPiece,
                    user
                )
            },
            storyOwner = fullStory.storyOwner.username,
            isCallerOwner = true,
            isStoryOpen = fullStory.isStoryOpen,
            storyEdited = fullStory.lastEdited!!,
            storyStarted = fullStory.created!!
        )
    }

    private fun mapStoryPieceToDto(storyPiece: StoryPiece, user: User) = StoryPieceResponse(
        pieceId = storyPiece.id!!,
        narrative = storyPiece.narrative,
        createdAt = storyPiece.createdAt!!,
        lastUpdatedAt = storyPiece.updatedAt!!,
        owner = storyPiece.user!!.username,
        isCallerOwner = user == storyPiece.user
    )

    private fun mapNewFullStoryRequestToFullStory(fullStoryRequest: FullStoryRequest, user: User) = FullStory(
        title = fullStoryRequest.title.trim(),
        topic = fullStoryRequest.topic.trim(),
        storyOwner = user,
        isStoryOpen = fullStoryRequest.isStoryOpen,
        created = LocalDateTime.now(),
        lastEdited = LocalDateTime.now()
    )

    private fun mapNewStoryPieceRequestToFullStory(
        storyPieceRequest: StoryPieceRequest,
        user: User,
        fullStory: FullStory
    ) = StoryPiece(
        narrative = storyPieceRequest.narrative,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        user = user,
        fullStory = fullStory
    )

    fun sendAddRequest(bearerToken: String, storyId: Long, storyPieceRequest: StoryPieceRequest) {
        val fullStory = findStoryById(id = storyId)
        val user = userService.findUserByBearerToken(bearerToken)
        val storyPiece = mapNewStoryPieceRequestToFullStory(storyPieceRequest, user, fullStory)

        if (user == fullStory.storyOwner) {
            fullStory.addToCanon(storyPiece)
            fullStory.lastEdited = LocalDateTime.now()
        } else {
            fullStory.addPotentialPiece(storyPiece)
        }
        fullStoryRepository.save(fullStory)
    }


    fun deleteStoryPieceRequest(jwtToken: String, storyId: Long, pieceId: Long) {
        val user = userService.findUserByBearerToken(jwtToken)
        val story = findStoryById(storyId)
        val piece = findPieceById(pieceId)
        if (story.potentialPieces.contains(piece)) {

            if (user == story.storyOwner) {
                story.potentialPieces.remove(piece)
            } else throw Exception()

        } else throw NotFoundException()
    }


    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }


}