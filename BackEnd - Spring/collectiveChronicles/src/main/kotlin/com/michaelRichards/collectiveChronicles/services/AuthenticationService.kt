package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.config.JWTService
import com.michaelRichards.collectiveChronicles.dtos.requests.AuthenticateRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.models.Token
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.TokenRepository
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.apache.coyote.http11.Constants.a
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JWTService,
    private val tokenRepository: TokenRepository,
    private val authenticationManager: AuthenticationManager
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


        val savedUser = repository.save(user)
        val jwtToken = jwtService.generateToken(userDetails = user)


        saveUserToken(savedUser, jwtToken)

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
        revokeAllTokens(user)
        saveUserToken(user, jwtToken)
        return AuthenticationResponse(accessToken = jwtToken)
    }

    private fun revokeAllTokens(user: User){
        val validUserTokens = user.id?.let { tokenRepository.findAllValidTokensByUser(it) }
        if (validUserTokens != null) {
            if (validUserTokens.isEmpty()) return
            validUserTokens.forEach { token ->
                token.isRevoked = true
                token.isExpired = true
            }
            tokenRepository.saveAll(validUserTokens)
        }
    }

    private fun saveUserToken(user: User, token: String){

        val jwtToken = Token(
            user = user,
            token = token,
            isExpired = false,
            isRevoked = false
        )
        tokenRepository.save(jwtToken)
    }
}