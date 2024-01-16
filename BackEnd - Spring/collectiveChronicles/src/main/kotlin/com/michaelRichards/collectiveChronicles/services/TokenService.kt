package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.models.Token
import com.michaelRichards.collectiveChronicles.repositories.TokenRepository
import io.jsonwebtoken.security.InvalidKeyException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenRepository: TokenRepository
) {


    fun findToken(jwtToken: String) : Token = tokenRepository.findByToken(extractBearerToken(jwtToken)) ?: throw NotFoundException()

    fun extractBearerToken(token : String) = if (!token.startsWith("Bearer ")) throw InvalidKeyException("Must be bearer token") else token.removePrefix("Bearer ")

}