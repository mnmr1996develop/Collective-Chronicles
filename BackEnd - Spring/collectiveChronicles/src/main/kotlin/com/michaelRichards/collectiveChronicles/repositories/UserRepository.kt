package com.michaelRichards.collectiveChronicles.repositories

import com.michaelRichards.collectiveChronicles.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, UUID> {

    fun findByUsernameIgnoreCase(username: String): User?


    fun findByEmailIgnoreCase(email: String): User?



    @Transactional
    @Modifying
    @Query("update User u set u.firstName = ?1 where u.username = ?2")
    fun updateFirstNameByUsername(firstName: String, username: String): Int


    @Transactional
    @Modifying
    @Query("delete from User u where u.username = ?1")
    fun deleteByUsername(username: String): Int


    @Transactional
    @Modifying
    @Query("delete from User u where u.id = ?1")
    override fun deleteById(id: UUID)
}