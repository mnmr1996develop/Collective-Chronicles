package com.michaelRichards.collectiveChronicles.exceptions.storyExceptions

import com.michaelRichards.collectiveChronicles.dtos.responses.APIException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.InvalidKeyException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class StoryExceptionHandler {

    @ExceptionHandler(value = [IndexOutOfBoundsException::class])
    fun handleIndexOutOfBoundsException(exception: IndexOutOfBoundsException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INDEX_OUT_OF_BOUNDS"

        val apiException = APIException(
            httpStatus,
            "index out of bounds",
            reason,
            LocalDateTime.now()
        )
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [StoryExceptions.IndexOutOfBound::class])
    fun handleMaxStoriesReachedException(exception: StoryExceptions.IndexOutOfBound): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INDEX_OUT_OF_BOUNDS"
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "index out of bounds",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [StoryExceptions.MaxStoriesReached::class])
    fun handleMaxStoriesReachedException(exception: StoryExceptions.MaxStoriesReached): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "MAX_STORIES_REACHED"
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "max stories reached",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [StoryExceptions.NotFoundException::class])
    fun handleNotFoundException(exception: StoryExceptions.NotFoundException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.NOT_FOUND
        val reason = "NOT_FOUND"
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "not found",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }


}