package com.michaelRichards.collectiveChronicles.exceptions.authorizationExceptions

import com.michaelRichards.collectiveChronicles.dtos.responses.APIException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.InvalidKeyException
import jakarta.validation.ConstraintViolationException
import org.springframework.core.annotation.Order
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime


@ControllerAdvice
class AuthorizationExceptionHandler {

    @ExceptionHandler(value = [BadCredentialsException::class])
    fun handleBadCredentialsException(exception: BadCredentialsException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "BAD_CREDENTIALS",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleWebExchangeBindException(e: ConstraintViolationException): ResponseEntity<APIException> {
        val httpStatus = HttpStatus.BAD_REQUEST

        val firstViolation = e.constraintViolations.firstOrNull()


        val apiException =
            firstViolation?.let {
                APIException(
                    httpStatus,
                    it.message,
                    "BAD_DATA",
                    LocalDateTime.now()
                )
            }

        return ResponseEntity.status(httpStatus).body(apiException)
    }

    @ExceptionHandler(value = [InvalidKeyException::class])
    fun handleInvalidToken(exception: InvalidKeyException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.UNAUTHORIZED
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "TOKEN_INVALID",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<Any> {
        val httpStatus = HttpStatus.NOT_FOUND
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "NOT_FOUND",
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
                "INVALID_JWT_FORMAT",
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
            "INDEX_OUT_OF_BOUNDS",
            LocalDateTime.now()
        )
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.UnAuthorizedAction::class])
    fun handleUnAuthorizedActionException(exception: AuthorizationExceptions.UnAuthorizedAction): ResponseEntity<Any> {
        val httpStatus = HttpStatus.UNAUTHORIZED
        val reason = "UNAUTHORIZED_ACTION"
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.EmailTaken::class])
    fun handleEmailTaken(exception: AuthorizationExceptions.EmailTaken): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "EMAIL_TAKEN"

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
                "Email is taken",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.InvalidName::class])
    fun handleInvalidName(exception: AuthorizationExceptions.InvalidName): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "INVALID_NAME"

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
                "name is invalid",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.InvalidPassword::class])
    fun handleInvalidPassword(exception: AuthorizationExceptions.InvalidPassword): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val reason = "PASSWORD_INVALID"

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
                "password invalid",
                reason,
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.UsernameTaken::class])
    fun handleUsernameTaken(exception: AuthorizationExceptions.UsernameTaken): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "USERNAME_TAKEN",
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Username is taken",
                "USERNAME_TAKEN",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

    @ExceptionHandler(value = [AuthorizationExceptions.InvalidAge::class])
    fun handleInvalidAge(exception: AuthorizationExceptions.InvalidAge): ResponseEntity<Any>{
        val httpStatus = HttpStatus.BAD_REQUEST
        val apiException = exception.message?.let {
            APIException(
                httpStatus,
                it,
                "INVALID_AGE",
                LocalDateTime.now()
            )
        } ?: run {
            APIException(
                httpStatus,
                "Age is invalid",
                "INVALID_AGE",
                LocalDateTime.now()
            )
        }
        return ResponseEntity(apiException, httpStatus)
    }

}