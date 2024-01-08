package com.michaelRichards.collectiveChronicles.config

import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class SeedDataConfig(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner{

    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            val admin = User(
                firstName = "admin",
                lastName = "admin",
                email = "admin@gmail.com",
                username = "admin",
                password = passwordEncoder.encode("admin"),
                birthday = LocalDate.now().minusYears(30L),
                role = Role.ROLE_ADMIN,
                accountCreatedAt = LocalDateTime.now(),
            )

            userRepository.save(admin)

        }
    }
}