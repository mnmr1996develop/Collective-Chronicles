package com.michaelRichards.collectiveChronicles.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JWTService {

    companion object{
        const val JWT_SECRET_KEY = "0af8dc2d7c5e0e167d723d75ab45b965b7b50839058371b2a1bafb0d727415e0"
        const val JWT_EXPIRATION = 86400000L
        const val JWT_REFRESH_EXPIRATION = 604800000L
    }

    fun extractUsername(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    private fun <T> extractClaim(token: String, claimsResolvers: Function<Claims, T>): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolvers.apply(claims)
    }

    fun generateToken(userDetails: UserDetails,extraClaims: Map<String, Any> = mutableMapOf()): String =
        buildToken(extraClaims, userDetails, JWT_EXPIRATION)

    private fun extractAllClaims(token: String): Claims = Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .body


    private fun buildToken(extraClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long) = Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact()

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username
    }

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date = extractClaim(token) { obj: Claims -> obj.expiration }
}