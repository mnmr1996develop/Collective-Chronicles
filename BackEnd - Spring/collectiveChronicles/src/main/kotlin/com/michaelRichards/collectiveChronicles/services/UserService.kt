package com.michaelRichards.collectiveChronicles.services

import com.michaelRichards.collectiveChronicles.dtos.responses.AdminDetailResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.UserDetailsDTO
import com.michaelRichards.collectiveChronicles.models.ProfileImage
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.ProfileImageRepository
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import com.michaelRichards.collectiveChronicles.utils.ImageUtils
import com.michaelRichards.collectiveChronicles.utils.Variables
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val profileImageRepository: ProfileImageRepository,
    private val jwtService: JWTService,
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

    fun extractUsernameFromBearerToken(bearerToken: String) = jwtService.extractUsername(bearerToken.removePrefix(Variables.BEARER))

    fun nullableFindByUsername(username: String): User? = userRepository.findByUsernameIgnoreCase(username)

    fun nullableFindByEmail(email: String): User? = userRepository.findByEmailIgnoreCase(email)

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

    fun getAllUsers(): List<UserDetailsDTO> = userRepository.findAll().map { user -> userToUserDTO(user) }

    fun userToUserDTO(user: User) : UserDetailsDTO =
        UserDetailsDTO(
            firstName = user.firstName,
            lastName = user.lastName,
            username = user.username,
            birthday = user.birthday!!,
            accountCreated = user.accountCreatedAt!!
    )

    fun deleteByUsername(username: String){
        val user = findByUsername(username)
        userRepository.save(user)
    }

    fun deleteUserByToken(jwtToken: String) = deleteByUsername(extractUsernameFromBearerToken(bearerToken = jwtToken))
    fun getUserDetails(jwtToken: String): UserDetailsDTO  = userToUserDTO(findUserByBearerToken(jwtToken))

    fun uploadProfileImage(jwtToken: String, profileImage: MultipartFile): UserDetailsDTO {
        val user = findUserByBearerToken(jwtToken)

        user.profileImage?.let {img ->
            img.image = ImageUtils.compressImage(profileImage.bytes)
            img.type= profileImage.contentType!!
            profileImageRepository.save(img)
        } ?: {
            val tmpProfileImage = ProfileImage(
                type = profileImage.contentType!!,
                image = ImageUtils.compressImage(profileImage.bytes),
                user = user
            )
            user.profileImage = tmpProfileImage
        }


        userRepository.save(user)
        return userToUserDTO(user)
    }

    fun downloadImage(jwtToken: String): Pair<ByteArray, String>{
        val user = findUserByBearerToken(jwtToken)

        val image = ImageUtils.decompressImage(user.profileImage!!.image)
        return Pair(image, user.profileImage!!.type)
    }


}