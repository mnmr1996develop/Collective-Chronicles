package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.requests.AuthenticateRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.models.ProfileImage
import com.michaelRichards.collectiveChronicles.services.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

private const val BASE_PATH = "/api/v1/auth"

@RestController
@RequestMapping(BASE_PATH)
class AuthController(
    private val authenticationService: AuthenticationService
) {



    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest
    ): ResponseEntity<AuthenticationResponse> = ResponseEntity.ok().body(authenticationService.register(registerRequest = registerRequest))


    @PostMapping("/login")
    fun login(
        @RequestBody registerRequest: AuthenticateRequest
    ): ResponseEntity<AuthenticationResponse> = ResponseEntity.ok(authenticationService.login(registerRequest))

    @PostMapping("/refresh-token")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) = authenticationService.refreshToken(request, response)

    @PostMapping("/authenticate")
    fun login(
        @RequestHeader("Authorization") authToken: String
    ): ResponseEntity<Unit> = ResponseEntity.ok(authenticationService.authenticateToken(authToken))

}