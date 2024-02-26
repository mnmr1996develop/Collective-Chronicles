package com.michaelRichards.collectiveChronicles.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.michaelRichards.collectiveChronicles.dtos.requests.AuthenticateRequest
import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.exceptions.authorizationExceptions.AuthorizationExceptions
import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.models.Token
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.TokenRepository
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import com.michaelRichards.collectiveChronicles.utils.Variables
import io.jsonwebtoken.security.InvalidKeyException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class AuthenticationService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JWTService,
    private val tokenRepository: TokenRepository,
    private val tokenService: TokenService,
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager
) {

    fun register(registerRequest: RegisterRequest, role: Role = Role.ROLE_USER): AuthenticationResponse {
        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            username = registerRequest.username,
            birthday = registerRequest.birthday,
            accountCreatedAt = LocalDateTime.now(),
            password = passwordEncoder.encode(registerRequest.password),
            role = role
        )

        verifyUser(user)


        val savedUser = repository.save(user)
        val jwtToken = jwtService.generateToken(userDetails = user)
        val refreshToken = jwtService.generateRefreshToken(user)


        saveUserToken(savedUser, jwtToken)

        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
    }

    private fun verifyEmail(email: String) {
        val user = userService.nullableFindByEmail(email)
        if (user  != null){
            throw AuthorizationExceptions.EmailTaken(email)
        }
    }

    private fun verifyUser(user: User) {

        verifyName(user.firstName)
        verifyName(user.lastName)
        verifyUsername(user.username)
        verifyEmail(user.email)
        user.birthday?.let { verifyAge(birthday = it) } ?: throw AuthorizationExceptions.UnAuthorizedAction("birthday isn't present")
    }

    private fun verifyName(name: String) {
        if (name.length  > 20 || name.length < 2){
            throw AuthorizationExceptions.InvalidName(name)
        }
    }

    private fun verifyUsername(username: String){

        if (username.length < 20){

        }

        val user = userService.nullableFindByUsername(username)
        if (user != null)
            throw AuthorizationExceptions.UsernameTaken(username)
    }

    private fun verifyAge(birthday: LocalDate){
        val age = ChronoUnit.YEARS.between(birthday, LocalDate.now())

        if (age < 13 || age > 150)
            throw AuthorizationExceptions.InvalidAge(age.toInt())
    }

    private fun verifyPassword(password: String) {
        if (!password.contains(Regex("[a-z]")))
            throw AuthorizationExceptions.InvalidPassword("Password Must Contain Lowercase")
        if (!password.contains(Regex("[A-Z]")))
            throw AuthorizationExceptions.InvalidPassword("Password Must Contain Uppercase")
        if (!password.contains(Regex("\\d")))
            throw AuthorizationExceptions.InvalidPassword("Password Must Contain Number")
        if (!password.contains(Regex("[@\$!%*#?&_]")))
            throw AuthorizationExceptions.InvalidPassword("Password Must Contain Special Character")
    }


    fun login(authenticationRequest: AuthenticateRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
        )

        val user = repository.findByUsernameIgnoreCase(authenticationRequest.username)
            ?: throw UsernameNotFoundException("${authenticationRequest.username} Invalid username")

        val jwtToken = jwtService.generateToken(userDetails = user)
        val refreshToken = jwtService.generateRefreshToken(user)
        //revokeAllTokens(user)
        saveUserToken(user, jwtToken)

        return AuthenticationResponse(accessToken = jwtToken, refreshToken = refreshToken)
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


    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(Variables.BEARER)) {
            return
        }
        val refreshToken: String = authHeader.substring(7)
        val username: String = jwtService.extractUsername(refreshToken)


        if (username.isNotEmpty()){
            val userDetails = this.repository.findByUsernameIgnoreCase(username) ?: throw UsernameNotFoundException("$username not found")
            if (jwtService.isTokenValid(refreshToken, userDetails)) run {
                val accessToken = jwtService.generateToken(userDetails)
                revokeAllTokens(userDetails)
                saveUserToken(userDetails, accessToken)
                val authResponse = AuthenticationResponse(
                    accessToken, refreshToken
                )

                ObjectMapper().writeValue(response.outputStream, authResponse )
            }
        }
    }



    fun authenticateToken(jwtToken: String){

        if (!tokenService.findToken(jwtToken).isTokenValid()) throw InvalidKeyException("Token not valid")
    }


}