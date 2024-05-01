package com.github.mukiva.weather_data.utils

sealed class RequestResult<out E : Any>(internal val data: E? = null) {
    class InProgress<E : Any>(data: E? = null) : RequestResult<E>(data)
    class Success<E : Any>(data: E) : RequestResult<E>(data)
    class Error<E : Any>(data: E? = null, val error: Throwable? = null) : RequestResult<E>()
}

internal fun <T : Any> Result<T>.asRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(getOrThrow())
        isFailure -> RequestResult.Error()
        else -> error("Unreachable code")
    }
}

internal fun <I : Any, O : Any> RequestResult<I>.map(transform: (I) -> O): RequestResult<O> {
    val outData = transform(checkNotNull(data))
    return when(this) {
        is RequestResult.Error -> RequestResult.Error()
        is RequestResult.InProgress -> RequestResult.InProgress(outData)
        is RequestResult.Success -> RequestResult.Success(outData)
    }
}