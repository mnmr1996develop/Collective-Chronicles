package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.responses.UserDetailsDTO
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.random.Random

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails =
        username?.let { userRepository.findByUsernameIgnoreCase(it) }
            ?: throw UsernameNotFoundException("$username not found")

    fun save(user: User): User {
        if (user.id == null) {
            user.accountCreatedAt = LocalDateTime.now()
        }
        return userRepository.save(user)
    }

    fun nullableFindByUsername(username: String): User? = userRepository.findByUsernameIgnoreCase(username)

    fun findByUsername(username: String): User =
        nullableFindByUsername(username) ?: throw UsernameNotFoundException("$username not found")

    fun getAllUserDetails(): List<UserDetailsDTO> = userRepository.findAll().map { user: User ->
        UserDetailsDTO(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            birthday = user.birthday!!,
            friends = Random.nextInt(0, 500)
        )
    }


}