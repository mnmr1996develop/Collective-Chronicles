package com.michaelrichards.collectivechronicles.repositories.results

sealed class AuthenticationResults<T> (val data: T? = null, val error: Exception? = null) {

    class Authenticated<T>(data: T? = null ): AuthenticationResults<T>(data)

    class TimeOutError<T>(): AuthenticationResults<T>()

    class Unauthenticated<T>(data: T? = null): AuthenticationResults<T>(data = data)

    class UnknownError<T>(error: Exception? = null): AuthenticationResults<T>(error = error)
    class Loading<T>(): AuthenticationResults<T>()
}