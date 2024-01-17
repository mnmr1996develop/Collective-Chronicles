package com.michaelrichards.collectivechronicles.repositories.auth

import android.content.SharedPreferences
import android.util.Log
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

private const val TAG = "AuthRepositoryImpl"

class AuthRepositoryImpl(
    private val api: AuthAPI,
    private val prefs: SharedPreferences
) : AuthRepository {

    companion object{
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val BEARER = "Bearer"
        private const val TIME_OUT_VAL = 10000L
    }


    override suspend fun login(authenticationRequest: AuthenticationRequest): AuthenticationResults<String> {
        return try {
            withTimeout(TIME_OUT_VAL) {
                val request = api.login(authenticationRequest)
                prefs.edit()
                    .putString(ACCESS_TOKEN, "$BEARER ${request.accessToken}")
                    .putString(REFRESH_TOKEN, "$BEARER ${request.refreshToken}")
                    .apply()
                AuthenticationResults.Authenticated()
            }
        } catch (e: HttpException) {
            AuthenticationResults.Unauthenticated("Bad Login")
        } catch (e: TimeoutCancellationException) {
            AuthenticationResults.TimeOutError()
        } catch (e: Exception) {
            AuthenticationResults.UnknownError(e)
        }
    }

    override suspend fun register(registrationRequest: RegistrationRequest): AuthenticationResults<String> {
        return try {
            withTimeout(TIME_OUT_VAL) {
                val request = api.register(registrationRequest)
                prefs.edit()
                    .putString(ACCESS_TOKEN, "$BEARER ${request.accessToken}")
                    .putString(REFRESH_TOKEN, "$BEARER ${request.accessToken}")
                    .apply()
                AuthenticationResults.Authenticated()
            }

        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 403) {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type

                val errorResponse: ErrorResponse? =
                    gson.fromJson(e.response()?.errorBody()!!.charStream(), type)

                AuthenticationResults.Unauthenticated(data = errorResponse?.message)
            } else {
                AuthenticationResults.Unauthenticated()
            }
        } catch (e: TimeoutCancellationException) {
            AuthenticationResults.TimeOutError()
        } catch (e: Exception) {
            AuthenticationResults.UnknownError(e)
        }
    }

    override suspend fun authenticate(): AuthenticationResults<String> {
        return try {
            withTimeout(TIME_OUT_VAL){
                val token = prefs.getString(ACCESS_TOKEN, null)
                if (token == null) {
                    AuthenticationResults.Unauthenticated()
                } else {
                    api.authenticate(token = token)
                    AuthenticationResults.Authenticated()
                }
            }
        } catch (e: TimeoutCancellationException){
            Log.d(TAG, "authenticate: ${e.message}", )
            AuthenticationResults.TimeOutError()

        }
        catch (e: HttpException) {
            Log.d(TAG, "authenticate: httpException")
            if (e.code() == 401 || e.code() == 403) {
                refresh()
            } else {
               AuthenticationResults.UnknownError()
            }
        }  catch (e: Exception) {
           AuthenticationResults.UnknownError()
        }
    }

    override suspend fun logout(): AuthenticationResults<String> {
        return try {
            withTimeout(TIME_OUT_VAL){
                val refreshToken = prefs.getString(REFRESH_TOKEN, null)
                val accessToken = prefs.getString(ACCESS_TOKEN, null)
                if (accessToken != null && refreshToken != null){
                    api.logout(accessToken)
                }
                prefs.edit().remove(ACCESS_TOKEN).apply()
                prefs.edit().remove(REFRESH_TOKEN).apply()
                AuthenticationResults.Unauthenticated()
            }
        }catch (e: TimeoutCancellationException){
            AuthenticationResults.TimeOutError()
        }catch (e: Exception){
            AuthenticationResults.UnknownError()
        }
    }

    override suspend fun refresh(): AuthenticationResults<String> {
        return try {
            withTimeout(TIME_OUT_VAL){
                val token = prefs.getString(REFRESH_TOKEN, null)
                if (token == null){
                    AuthenticationResults.Unauthenticated()
                }else{
                    val request = api.refresh(token = token)
                    prefs.edit()
                        .putString(ACCESS_TOKEN, "$BEARER ${request.accessToken}")
                        .putString(REFRESH_TOKEN, "$BEARER ${request.accessToken}")
                        .apply()
                    AuthenticationResults.Authenticated()
                }
            }
        } catch (e: TimeoutCancellationException){
            AuthenticationResults.TimeOutError()
        }
        catch (e: HttpException){
            AuthenticationResults.Unauthenticated()
        }
    }
}