package com.github.mukiva.weather_data.utils

interface IDataMergeStrategy<E> {
    fun merge(local: E, remote: E): E
}

internal class ForecastMergeStrategy<T : Any> : IDataMergeStrategy<RequestResult<T>> {
    override fun merge(local: RequestResult<T>, remote: RequestResult<T>): RequestResult<T> {
        return when {
            local is RequestResult.InProgress && remote is RequestResult.InProgress ->
                merge(local, remote)
            local is RequestResult.InProgress && remote is RequestResult.Success ->
                RequestResult.InProgress(remote.data)
            local is RequestResult.Success && remote is RequestResult.InProgress ->
                RequestResult.InProgress(local.data)
            local is RequestResult.Success && remote is RequestResult.Success ->
                RequestResult.Success(checkNotNull(remote.data))
            local is RequestResult.Error && remote is RequestResult.Error ->
                RequestResult.Error(remote.data, remote.error)
            local is RequestResult.Error && remote is RequestResult.Success ->
                RequestResult.Error(remote.data, local.error)
            local is RequestResult.Success && remote is RequestResult.Error ->
                RequestResult.Success(checkNotNull(local.data))
            local is RequestResult.Error && remote is RequestResult.InProgress ->
                RequestResult.InProgress(remote.data)
            local is RequestResult.InProgress && remote is RequestResult.Error ->
                RequestResult.InProgress(local.data)
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
}