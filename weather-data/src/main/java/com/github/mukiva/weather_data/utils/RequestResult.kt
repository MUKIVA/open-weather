package com.github.mukiva.weather_data.utils

sealed class RequestResult<out E : Any>(val data: E? = null) {
    class InProgress<E : Any>(data: E? = null) : RequestResult<E>(data)
    class Success<E : Any>(data: E) : RequestResult<E>(data)
    class Error<E : Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>(data)
}

internal fun <T : Any> Result<T>.asRequestResult(): RequestResult<T> {
    val data = getOrNull()
    return when {
        isSuccess && data != null -> RequestResult.Success(data)
        else -> RequestResult.Error(data = data, exceptionOrNull())
    }
}

fun <I : Any, O : Any> RequestResult<I>.map(transform: (I) -> O): RequestResult<O> {
    return when(this) {
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.InProgress -> RequestResult.InProgress(data?.let(transform))
        is RequestResult.Success -> RequestResult.Success(transform(checkNotNull(data)))
    }
}