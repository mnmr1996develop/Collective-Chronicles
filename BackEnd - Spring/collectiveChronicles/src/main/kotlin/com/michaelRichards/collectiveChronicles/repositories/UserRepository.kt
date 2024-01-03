package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.MUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<MUser, UUID> {


    fun findByUsernameIgnoreCase(username: String): MUser?
}