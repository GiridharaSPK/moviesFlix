package com.giridharaspk.movieflix.data.api

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null,
    val t: Throwable? = null
) {
    class Loading<T> : ApiResult<T>()
    class Success<T>(data: T) : ApiResult<T>(data = data)
    class Failure<T>(message: String?) : ApiResult<T>(message = message)
    class NetworkError<T>(t: Throwable?) : ApiResult<T>(t = t)
    class Error<T>(t: Exception) : ApiResult<T>(t = t)
}