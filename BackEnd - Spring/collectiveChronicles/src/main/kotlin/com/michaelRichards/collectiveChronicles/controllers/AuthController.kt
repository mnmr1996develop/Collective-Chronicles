package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.requests.AuthenticateRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.services.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val BASE_PATH = "/api/v1/auth"

@RestController
@RequestMapping(BASE_PATH)
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ) : ResponseEntity<AuthenticationResponse> = ResponseEntity.ok(authenticationService.register(registerRequest))

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody registerRequest: AuthenticateRequest
    ) : ResponseEntity<AuthenticationResponse> = ResponseEntity.ok(authenticationService.authenticate(registerRequest))

}