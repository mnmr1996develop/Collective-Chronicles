package com.michaelrichards.collectivechronicles.network

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.dtos.responses.JWTAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface AuthAPI {

    companion object{
        private const val AUTHORIZATION = "Authorization"
    }

    @POST("auth/login")
    suspend fun login(@Body request: AuthenticationRequest): JWTAuthenticationResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegistrationRequest): JWTAuthenticationResponse

    @POST("auth/refresh-token")
    suspend fun refresh(@Header(AUTHORIZATION) token: String): JWTAuthenticationResponse

    @POST("auth/authenticate")
    suspend fun authenticate(@Header(AUTHORIZATION) token: String): JWTAuthenticationResponse

    @POST("auth/logout")
    suspend fun logout(@Header(AUTHORIZATION) token: String)
}
