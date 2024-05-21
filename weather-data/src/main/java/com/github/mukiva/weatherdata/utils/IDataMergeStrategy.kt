package com.github.mukiva.weatherdata.utils

import android.util.Log

interface IDataMergeStrategy<E> {
    fun merge(local: E, remote: E): E
}

internal class ForecastMergeStrategy<T : Any> : IDataMergeStrategy<RequestResult<T>> {
    override fun merge(local: RequestResult<T>, remote: RequestResult<T>): RequestResult<T> {
        return when {
            local is RequestResult.InProgress && remote is RequestResult.InProgress -> {
                Log.d("STATE", "PP")
                merge(local, remote)
            }
            local is RequestResult.InProgress && remote is RequestResult.Success -> {
                Log.d("STATE", "PS")
                RequestResult.Success(checkNotNull(remote.data))
            }
            local is RequestResult.Success && remote is RequestResult.InProgress -> {
                Log.d("STATE", "SP")
                RequestResult.InProgress(local.data)
            }
            local is RequestResult.Success && remote is RequestResult.Success -> {
                Log.d("STATE", "SS")
                RequestResult.Success(checkNotNull(remote.data))
            }
            local is RequestResult.Error && remote is RequestResult.Error -> {
                Log.d("STATE", "EE")
                RequestResult.Error(remote.data, remote.error)
            }
            local is RequestResult.Error && remote is RequestResult.Success -> {
                Log.d("STATE", "ES")
                RequestResult.Error(remote.data, local.error)
            }
            local is RequestResult.Success && remote is RequestResult.Error -> {
                Log.d("STATE", "SE")
                RequestResult.Success(checkNotNull(local.data))
            }
            local is RequestResult.Error && remote is RequestResult.InProgress -> {
                Log.d("STATE", "EP")
                RequestResult.InProgress(remote.data)
            }
            local is RequestResult.InProgress && remote is RequestResult.Error -> {
                Log.d("STATE", "PE")
                RequestResult.InProgress(local.data)
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
}
