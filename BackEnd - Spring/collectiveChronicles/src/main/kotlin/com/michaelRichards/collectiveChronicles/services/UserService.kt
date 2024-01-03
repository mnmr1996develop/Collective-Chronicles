package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails =
        username?.let { userRepository.findByUsernameIgnoreCase(it) }
            ?: throw UsernameNotFoundException("$username not found")
}