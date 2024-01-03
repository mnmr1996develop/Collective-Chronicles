package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.services.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val BASE_PATH = "api/v1/users"

@RestController
@RequestMapping(BASE_PATH)
class UserController(
    private val userService: UserService
) {
}