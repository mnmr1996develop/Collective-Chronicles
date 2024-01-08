package com.michaelRichards.collectiveChronicles.dtos.responses

import java.time.LocalDate

data class UserDetailsDTO(
    val firstName: String,
    val lastName: String,
    val username: String,
    val birthday: LocalDate,
    val friends: Int
)
