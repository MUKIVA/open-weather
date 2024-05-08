package com.github.mukiva.weather_data.utils

import android.util.Log

interface IDataMergeStrategy<E> {
    fun merge(local: E, remote: E): E
}

internal class ForecastMergeStrategy<T : Any> : IDataMergeStrategy<RequestResult<T>> {
    override fun merge(local: RequestResult<T>, remote: RequestResult<T>): RequestResult<T> {
        return when {
            local is RequestResult.InProgress && remote is RequestResult.InProgress -> {
                Log.d("P", "PP")
                merge(local, remote)
            }
            local is RequestResult.InProgress && remote is RequestResult.Success -> {
                Log.d("P", "PS")
                merge(local, remote)
            }

            local is RequestResult.Success && remote is RequestResult.InProgress ->{
                Log.d("P", "SP")
                merge(local, remote)
            }
            local is RequestResult.Success && remote is RequestResult.Success ->{
                Log.d("P", "SS")
                merge(local, remote)
            }
            local is RequestResult.Error && remote is RequestResult.Error ->{
                Log.d("P", "EE")
                merge(local, remote)
            }
            local is RequestResult.Error && remote is RequestResult.Success ->{
                Log.d("P", "ES")
                merge(local, remote)
            }
            local is RequestResult.Success && remote is RequestResult.Error ->{
                Log.d("P", "SE")
                merge(local, remote)
            }
            local is RequestResult.Error && remote is RequestResult.InProgress -> {
                Log.d("P", "EP")
                merge(local, remote)
            }
            local is RequestResult.InProgress && remote is RequestResult.Error ->{
                Log.d("P", "PE")
                merge(local, remote)
            }
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

    private fun merge(
        local: RequestResult.InProgress<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(remote.data)
    }

    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.InProgress(local.data)
    }

    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Success(checkNotNull(remote.data))
    }

    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(remote.data, remote.error)
    }

    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.Success<T>
    ): RequestResult<T> {
        return RequestResult.Error(remote.data, local.error)
    }

    private fun merge(
        local: RequestResult.Success<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Success(checkNotNull(local.data))
    }

    private fun merge(
        local: RequestResult.Error<T>,
        remote: RequestResult.InProgress<T>
    ): RequestResult<T> {
        return RequestResult.Error(remote.data, local.error)
    }

    private fun merge(
        local: RequestResult.InProgress<T>,
        remote: RequestResult.Error<T>
    ): RequestResult<T> {
        return RequestResult.Error(local.data, remote.error)
    }
}