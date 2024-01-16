package com.michaelrichards.collectivechronicles.repositories.auth

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.michaelrichards.collectivechronicles.R

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.dtos.responses.ErrorResponse
import com.michaelrichards.collectivechronicles.network.AuthAPI
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.lang.Exception

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl(
    private val api: AuthAPI,
    private val prefs: SharedPreferences
) : AuthRepository {


    override suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResults<String> {
        return try {
            withTimeout(10000) {
                val request = api.login(authenticationRequest)
                prefs.edit()
                    .putString("accessToken", "Bearer ${request.accessToken}")
                    .putString("refreshToken", "Bearer ${request.refreshToken}")
                    .apply()
                AuthenticationResults.Authenticated()
            }
        } catch (e: HttpException) {
            AuthenticationResults.BadAuthenticationData("Bad Login")
        } catch (e: TimeoutCancellationException) {
            AuthenticationResults.TimeOutError("Too Long")
        } catch (e: Exception) {
            Log.e(TAG, "login: $e")
            AuthenticationResults.UnknownError(e)
        }
    }

    override suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResults<String> {
        return try {
            withTimeout(10000) {
                val request = api.register(registrationRequest)
                prefs.edit()
                    .putString("accessToken", "Bearer ${request.accessToken}")
                    .putString("refreshToken", "Bearer ${request.accessToken}")
                    .apply()
                AuthenticationResults.Authenticated()
            }

        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type

                val errorResponse: ErrorResponse? =
                    gson.fromJson(e.response()?.errorBody()!!.charStream(), type)

                AuthenticationResults.BadAuthenticationData(data = errorResponse?.message)
            } else {
                AuthenticationResults.BadAuthenticationData()
            }
        } catch (e: TimeoutCancellationException) {
            AuthenticationResults.TimeOutError("Too Long")
        } catch (e: Exception) {
            AuthenticationResults.UnknownError(e)
        }
    }

    override suspend fun authenticate(): AuthenticationResults<String> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): AuthenticationResults<String> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh(): AuthenticationResults<String> {
        TODO("Not yet implemented")
    }


}