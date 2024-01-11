package com.michaelrichards.collectivechronicles.repositories.results

sealed class ApiSuccessFailState<T> (val data: T? = null, val error: Exception? = null) {
    class Success<T>(data: T? = null ): ApiSuccessFailState<T>(data)
    class BadRequest<T>(error: Exception? = null): ApiSuccessFailState<T>(error = error)
    class Loading<T>: ApiSuccessFailState<T>()
}