package com.michaelRichards.collectiveChronicles.exceptions

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
class ApiExceptionHandler {

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handleBadCredentialsException(exception: BadCredentialsException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [InvalidKeyException::class])
    fun handleInvalidToken(exception: InvalidKeyException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.UNAUTHORIZED
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleInvalidToken(exception: NotFoundException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.NOT_FOUND
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [MalformedJwtException::class])
    fun handleMalformedJwtException(exception: MalformedJwtException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [IndexOutOfBoundsException::class])
    fun handleIndexOutOfBoundsException(exception: IndexOutOfBoundsException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = APIException(
            httpStatus,
            "index out of bounds",
            LocalDateTime.now()
        )
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [CustomExceptions.UnAuthorizedAction::class])
    fun handleUnAuthorizedActionException(exception: CustomExceptions.UnAuthorizedAction): ResponseEntity<Any> {
        val httpStatus = HttpStatus.UNAUTHORIZED
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [CustomExceptions.IndexOutOfBound::class])
    fun handleCustomIndexOutOfBounds(exception: CustomExceptions.IndexOutOfBound): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

}