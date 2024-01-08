package com.michaelRichards.collectiveChronicles.models

import jakarta.persistence.*

@Entity
class Token(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    var token: String =  "",

    @Enumerated(value = EnumType.STRING)
    val tokenType: TokenType = TokenType.BEARER,

    var isExpired: Boolean = false,

    var isRevoked: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
) {

    fun isTokenValid() = !isRevoked && !isExpired



}

enum class TokenType {
    BEARER
}
