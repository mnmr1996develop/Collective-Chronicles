package com.michaelrichards.collectivechronicles.network

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.dtos.responses.JWTAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface AuthAPI {
    @POST("auth/login")
    suspend fun login(@Body request: AuthenticationRequest): JWTAuthenticationResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegistrationRequest): JWTAuthenticationResponse
}
