package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface TokenRepository : JpaRepository<Token, Long> {

    @Query("select t from Token t inner join User u on t.user.id = u.id where u.id = :userId and (t.isExpired = false or t.isRevoked = false)")
    fun findAllValidTokensByUser(userId: UUID): List<Token>

    fun findByToken(token: String): Token?
}