package com.michaelrichards.collectivechronicles.repositories.results

sealed class RegistrationResults<T> (val data: T? = null, val error: Exception? = null) {

    class Authenticated<T>(data: T? = null ): RegistrationResults<T>(data)

    class TimeOutError<T>(data: T): RegistrationResults<T>(data = data)

    class BadAuthenticationData<T>(data: T? = null, error: Exception? = null): RegistrationResults<T>(data = data, error = error)

    class Loading<T>(): RegistrationResults<T>()
}