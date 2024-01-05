package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.config.JWTService
import com.michaelRichards.collectiveChronicles.dtos.requests.AuthenticateRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.apache.coyote.http11.Constants.a
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    val repository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JWTService,
    val authenticationManager: AuthenticationManager
) {

    fun register(registerRequest: RegisterRequest): AuthenticationResponse {
        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            username = registerRequest.username,
            birthday = registerRequest.birthday,
            password = passwordEncoder.encode(registerRequest.password),
            role = Role.ROLE_USER
        )
        repository.save(user)
        val jwtToken = jwtService.generateToken(userDetails = user)
        return AuthenticationResponse(accessToken = jwtToken)
    }

    fun authenticate(authenticationRequest: AuthenticateRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = repository.findByUsernameIgnoreCase(authenticationRequest.username)
            ?: throw UsernameNotFoundException("${authenticationRequest.username} Invalid username")
        val jwtToken = jwtService.generateToken(userDetails = user)
        return AuthenticationResponse(accessToken = jwtToken)
    }
}