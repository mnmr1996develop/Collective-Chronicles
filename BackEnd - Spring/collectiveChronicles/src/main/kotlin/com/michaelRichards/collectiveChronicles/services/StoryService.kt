package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.requests.FullStoryRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.StoryPieceRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.OwnerStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.PublicStoryResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.StoryPieceResponse
import com.michaelRichards.collectiveChronicles.exceptions.CustomExceptions
import com.michaelRichards.collectiveChronicles.models.FullStory
import com.michaelRichards.collectiveChronicles.models.StoryPiece
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.FullStoryRepository
import com.michaelRichards.collectiveChronicles.repositories.StoryPieceRepository
import jakarta.transaction.Transactional
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    fun findPieceById(id: Long): StoryPiece = storyPieceRepository.findById(id).orElseThrow { NotFoundException() }

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
        pageNumber: Int?,
        pageSize: Int?,
        isAscending: Boolean?
    ): List<OwnerStoryResponse> {
        val user = userService.findUserByBearerToken(jwtToken)

        val stories =
            if (pageNumber == null || pageSize == null) fullStoryRepository.findByStoryOwnerOrderByStoryLastEditedDesc(
                user
            ) else {
                if (pageNumber < 0 ) throw CustomExceptions.IndexOutOfBound(pageNumber, "pageNumber")
                if (pageSize < 1 || pageSize > 100) throw CustomExceptions.IndexOutOfBound(pageSize, "pageSize")

                val pageable: Pageable =
                    if (isAscending == null)
                        PageRequest.of(pageNumber, pageSize)
                    else
                        PageRequest.of(pageNumber, pageSize,
                        if (isAscending) Sort.by("storyLastEdited").ascending() else Sort.by("storyLastEdited").descending()
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

        println("size of story ${story.canon.size}")

        if (pieceId1 < story.canon.size && pieceId2 < story.canon.size) {
            story.canon.swap(pieceId1, pieceId2)
            fullStoryRepository.save(story)
        } else {
            throw IndexOutOfBoundsException()
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
        storyLastEdited = fullStory.storyLastEdited!!,
        storyOwner = fullStory.storyOwner!!.username,
        canon = fullStory.canon.map { storyPiece -> mapStoryPieceToDto(storyPiece, user) },
        storyStarted = fullStory.storyStarted!!,
        title = fullStory.title,
        topic = fullStory.topic,
        isCallerOwner = user == fullStory.storyOwner
    )

    private fun mapOwnerStoryToDTO(fullStory: FullStory, user: User): OwnerStoryResponse {
        if (fullStory.storyOwner != user) throw CustomExceptions.UnAuthorizedAction("Unauthorized User Action")

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
            storyEdited = fullStory.storyLastEdited!!,
            storyStarted = fullStory.storyStarted!!
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
        storyStarted = LocalDateTime.now(),
        storyLastEdited = LocalDateTime.now()
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
            fullStory.storyLastEdited = LocalDateTime.now()
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