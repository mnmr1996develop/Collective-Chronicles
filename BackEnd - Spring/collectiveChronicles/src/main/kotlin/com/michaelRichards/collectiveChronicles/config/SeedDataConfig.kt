package com.michaelRichards.collectiveChronicles.config

import com.michaelRichards.collectiveChronicles.models.ProfileImage
import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.models.User
import com.michaelRichards.collectiveChronicles.repositories.ProfileImageRepository
import com.michaelRichards.collectiveChronicles.repositories.UserRepository
import com.michaelRichards.collectiveChronicles.utils.ImageUtils
import io.github.serpro69.kfaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.util.*
import kotlin.math.log
import kotlin.random.Random

@Component
class SeedDataConfig(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val profileImageRepository: ProfileImageRepository
) : CommandLineRunner {

    private val faker = Faker()
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
            createUsers()

        }
    }

    private fun createUsers() {
        val randomUser = (1..100).forEach { _ ->

            val isNameInEmail = Random.nextBoolean()

            var firstName = faker.name.firstName().filterNot { it.isWhitespace() }
            var lastName = faker.name.lastName().filterNot { it.isWhitespace() }
            var birthday = generateRandomBirthday()
            val email = if (isNameInEmail) faker.internet.email(name = "$firstName $lastName").filterNot { it.isWhitespace() } else faker.internet.email()


            while (userRepository.findByEmailIgnoreCase(email) != null){
                firstName =  faker.name.firstName().filterNot { it.isWhitespace() }
                lastName = faker.name.lastName().filterNot { it.isWhitespace() }
                birthday = generateRandomBirthday()
                if (isNameInEmail) faker.internet.email(name = "$firstName $lastName").filterNot { it.isWhitespace() } else faker.internet.email()
            }
            val username = generateRandomUsername(firstName, lastName, birthday)



            val user = User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                username = username,
                password = passwordEncoder.encode("Password2!"),
                birthday = birthday,
                role = Role.ROLE_USER,
                accountCreatedAt = generateRandomAccountCreated()
            )

            val savedUser = userRepository.save(user)
            saveProfileImage(savedUser)
        }
    }

    private fun saveProfileImage(user: User){
        val profileImage = ProfileImage(
            image = ImageUtils.compressImage(ImageUtils.createBasicProfileImage(user.firstName[0].uppercaseChar())),
            type = "image/png",
            user = user
        )
        user.profileImage = profileImage
        userRepository.save(user)
    }



    private fun generateRandomBirthday(): LocalDate {
        val start = LocalDate.of(1960, 1, 1)
        val end = LocalDate.of(2000, 12, 31)
        val randomDate =
            start.plusDays(Random.nextInt(end.toEpochDay().toInt() - start.toEpochDay().toInt() + 1).toLong())
        return randomDate
    }

    private fun generateRandomUsername(firstName: String, lastName: String, birthday: LocalDate): String {
        val randomInt = Random.nextInt(4)
        val randomFiller = faker.random.randomString()
        var randomUsername = ""
        when (randomInt) {
            0 -> randomUsername = "${firstName[0].uppercase(Locale.getDefault())}${
                faker.animal.name().filterNot { it.isWhitespace() }.lowercase(Locale.getDefault())
            }$lastName${birthday.year.toString().takeLast(2)}"

            1 -> randomUsername = "$firstName$lastName${birthday.year}"
            2 -> {
                var username = "${faker.artist.names()}${birthday.year.toString().takeLast(2)}fan".filterNot { it.isWhitespace() }
                while (userRepository.findByUsernameIgnoreCase(username) != null){
                    username = "${faker.artist.names()}${birthday.year.toString().takeLast(2)}fan".filterNot { it.isWhitespace() }
                }
                randomUsername = username
            }



            3 -> {
                var username = "${faker.basketball.players().filterNot { it.isWhitespace() }}_${Random.nextInt(10000)}"
                while (userRepository.findByUsernameIgnoreCase(username) != null) {
                    username ="${faker.basketball.players().filterNot { it.isWhitespace() }}_${Random.nextInt(10000)}"
                }
                randomUsername = username
            }

            else -> {
                var username = "${faker.bojackHorseman.characters()}$randomFiller".filterNot { it.isWhitespace() }
                while (userRepository.findByUsernameIgnoreCase(username) != null) {
                    username = "${faker.bojackHorseman.characters()}$randomFiller".filterNot { it.isWhitespace() }
                }
                randomUsername = username
            }

        }

        return randomUsername
    }

    private fun generateRandomAccountCreated(): LocalDateTime {
        val start = LocalDate.of(2015, Month.JANUARY, 1)
        val end = LocalDate.now()
        val randomDate =
            start.plusDays(Random.nextInt(end.toEpochDay().toInt() - start.toEpochDay().toInt() + 1).toLong())
        val randomTime = LocalTime.of(Random.nextInt(24), Random.nextInt(60), Random.nextInt(60))
        return LocalDateTime.of(randomDate, randomTime)
    }
}