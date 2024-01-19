package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.responses.AdminDetailResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.UserDetailsDTO
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val jwtService: JWTService
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

    fun findUserByBearerToken(bearerToken: String) = findByUsername(extractUsernameFromBearerToken(bearerToken))

    fun extractUsernameFromBearerToken(bearerToken: String) = jwtService.extractUsername(bearerToken.removePrefix("Bearer "))

    fun nullableFindByUsername(username: String): User? = userRepository.findByUsernameIgnoreCase(username)

    fun findByUsername(username: String): User =
        nullableFindByUsername(username) ?: throw UsernameNotFoundException("$username not found")

    fun getAllUserDetails(): List<UserDetailsDTO> = userRepository.findAll().map { user: User ->
        userToUserDTO(user)
    }

    fun lockAccount(username: String): AdminDetailResponse {
        val user = findByUsername(username)
        user.setIsAccountNonLocked(false)
        userRepository.save(user)

        return AdminDetailResponse(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            birthday = user.birthday!!,
            accountCreatedAt = user.accountCreatedAt!!
        )
    }

    fun unlockAccount(username: String): AdminDetailResponse {
        val user = findByUsername(username)
        user.setIsAccountNonLocked(true)
        userRepository.save(user)
        return AdminDetailResponse(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            birthday = user.birthday!!,
            accountCreatedAt = user.accountCreatedAt!!
        )
    }

    fun getAllUsers(): List<UserDetailsDTO> = userRepository.findAll().map { user: User -> userToUserDTO(user) }

    fun userToUserDTO(user: User) : UserDetailsDTO =
        UserDetailsDTO(

            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            birthday = user.birthday!!,
    )

    fun deleteByUsername(username: String){
        val user = findByUsername(username)
        userRepository.save(user)
    }

    fun deleteUserByToken(jwtToken: String) = deleteByUsername(extractUsernameFromBearerToken(bearerToken = jwtToken))


}