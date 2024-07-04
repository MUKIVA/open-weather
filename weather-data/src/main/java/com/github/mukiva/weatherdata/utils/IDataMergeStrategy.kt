package com.github.mukiva.weatherdata.utils

interface IDataMergeStrategy<E> {
    fun merge(local: E, remote: E): E
}

internal class ForecastMergeStrategy<T : Any> : IDataMergeStrategy<RequestResult<T>> {
    override fun merge(local: RequestResult<T>, remote: RequestResult<T>): RequestResult<T> {
        return when {
            local is RequestResult.InProgress && remote is RequestResult.InProgress ->
                merge(local, remote)
            local is RequestResult.InProgress && remote is RequestResult.Success ->
                merge(local, remote)
            local is RequestResult.Success && remote is RequestResult.InProgress ->
                merge(local, remote)
            local is RequestResult.Success && remote is RequestResult.Success ->
                merge(local, remote)
            local is RequestResult.Error && remote is RequestResult.Error ->
                merge(local, remote)
            local is RequestResult.Error && remote is RequestResult.Success ->
                merge(local, remote)
            local is RequestResult.Success && remote is RequestResult.Error ->
                merge(local, remote)
            local is RequestResult.Error && remote is RequestResult.InProgress ->
                merge(local, remote)
            local is RequestResult.InProgress && remote is RequestResult.Error ->
                merge(local, remote)
            else -> error("Not Implemented merge brunch")
        }
    }

    private fun merge(
        local: RequestResult.InProgress<T>,
        remote: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return when {
            remote.data != null -> RequestResult.InProgress(remote.data)
            else -> RequestResult.InProgress(local.data)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.InProgress<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(remote.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(remote.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(local.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.InProgress<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(local.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(remote.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(null, remote.cause)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Success(local.data)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(remote.data)
    }
}
