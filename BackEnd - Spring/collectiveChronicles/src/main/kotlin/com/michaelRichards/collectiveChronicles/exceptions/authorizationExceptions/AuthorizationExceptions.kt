package com.michaelRichards.collectiveChronicles.exceptions.authorizationExceptions

import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
sealed class AuthorizationExceptions(message: String) : Exception(message) {
    class UnAuthorizedAction(message: String): AuthorizationExceptions(message)
    class InvalidName(name: String): AuthorizationExceptions(message = "$name is invalid")
    class UsernameTaken(username: String): AuthorizationExceptions(message = "username: $username is already taken")
    class EmailTaken(email: String): AuthorizationExceptions(message = "email: $email is already taken")
    class InvalidAge(age: Int): AuthorizationExceptions(message = if (age < 13) "$age is too young" else "$age is invalid")
    class InvalidPassword(message: String): AuthorizationExceptions(message = message)
}