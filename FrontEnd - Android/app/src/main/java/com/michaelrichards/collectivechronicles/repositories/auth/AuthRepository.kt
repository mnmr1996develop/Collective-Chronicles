package com.michaelrichards.collectivechronicles.repositories.auth

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults

interface AuthRepository {

    suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResults<String>

    suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResults<String>

    suspend fun authenticate(): AuthenticationResults<String>

    suspend fun logout(): AuthenticationResults<String>

    suspend fun refresh(): AuthenticationResults<Unit>
}