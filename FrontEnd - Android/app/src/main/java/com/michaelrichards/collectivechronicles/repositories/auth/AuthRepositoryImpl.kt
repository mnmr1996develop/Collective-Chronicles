package com.michaelrichards.collectivechronicles.repositories.auth

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.dtos.responses.ErrorResponse
import com.michaelrichards.collectivechronicles.network.AuthAPI
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.lang.Exception

class AuthRepositoryImpl(
    private val api: AuthAPI,
    private val prefs: SharedPreferences
): AuthRepository {


    override suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResults<String> {
        return try {
            val request = api.login(authenticationRequest)
            prefs.edit()
                .putString("accessToken", "Bearer ${request.accessToken}")
                .putString("refreshToken", "Bearer ${request.accessToken}")
                .apply()
            AuthenticationResults.Authenticated()
        }catch (e: HttpException){
            if (e.code() == 400){
                AuthenticationResults.BadLoginData("")
            } else {
                AuthenticationResults.BadRequest()
            }
        }catch (e: Exception){
            AuthenticationResults.BadRequest()
        }
    }

    override suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResults<String> {
        return try {
            withTimeout(10000){
                val request = api.register(registrationRequest)
                prefs.edit()
                    .putString("accessToken", "Bearer ${request.accessToken}")
                    .putString("refreshToken", "Bearer ${request.accessToken}")
                    .apply()
                AuthenticationResults.Authenticated()
            }

        }catch (e: HttpException){
            if (e.code() == 401 || e.code() == 403){
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>(){}.type

                val errorResponse: ErrorResponse? = gson.fromJson(e.response()?.errorBody()!!.charStream(), type)

                AuthenticationResults.BadLoginData(data = errorResponse?.message)
            }else{
                AuthenticationResults.BadRequest(error = e)
            }
        }
        catch (e: TimeoutCancellationException){
            AuthenticationResults.TimeOutError("Too Long")
        }
        catch (e: Exception){
            AuthenticationResults.BadRequest(error = e)
        }
    }

    override suspend fun authenticate(): AuthenticationResults<String> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): AuthenticationResults<String> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh(): AuthenticationResults<Unit> {
        TODO("Not yet implemented")
    }


}