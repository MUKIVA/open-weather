package com.github.mukiva.weatherdata.utils

sealed class RequestResult<T> {
    data class InProgress<T>(val data: T? = null) : RequestResult<T>()
    data class Error<T>(val data: T? = null, val cause: Throwable?) : RequestResult<T>()
    data class Success<T>(val data: T) : RequestResult<T>()
}

internal fun <T : Any> Result<T>.asRequestResult(): RequestResult<T> {
    val data = getOrNull()
    return when {
        isSuccess && data != null -> RequestResult.Success(data)
        else -> RequestResult.Error(data = data, exceptionOrNull())
    }
}

fun <I, O> RequestResult<I>.map(transform: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Error -> RequestResult.Error(data?.let(transform), cause)
        is RequestResult.InProgress -> RequestResult.InProgress(data?.let(transform))
        is RequestResult.Success -> RequestResult.Success(transform(data))
    }
}
