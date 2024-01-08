package com.michaelRichards.collectiveChronicles.controllers

import com.michaelRichards.collectiveChronicles.dtos.requests.RegisterRequest
import com.michaelRichards.collectiveChronicles.dtos.responses.AdminDetailResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.AuthenticationResponse
import com.michaelRichards.collectiveChronicles.dtos.responses.UserDetailsDTO
import com.michaelRichards.collectiveChronicles.models.Role
import com.michaelRichards.collectiveChronicles.services.AuthenticationService
import com.michaelRichards.collectiveChronicles.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI


private const val BASE_PATH = "/api/v1/admins"

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(BASE_PATH)
class AdminController(
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UserDetailsDTO>> = ResponseEntity.ok(userService.getAllUsers())

    @PutMapping("/users/{username}/lockAccount")
    fun lockUserAccount(@PathVariable username: String): ResponseEntity<AdminDetailResponse> = ResponseEntity.ok(userService.lockAccount(username))

    @PutMapping("/users/{username}/unlockAccount")
    fun unlockUserAccount(@PathVariable username: String): ResponseEntity<AdminDetailResponse> = ResponseEntity.ok(userService.unlockAccount(username))



    @PostMapping
    fun registerAdmin(@RequestBody signUpRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        val uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(BASE_PATH).toUriString())
        return ResponseEntity.created(uri).body(authenticationService.register(signUpRequest, Role.ROLE_ADMIN))
    }
}