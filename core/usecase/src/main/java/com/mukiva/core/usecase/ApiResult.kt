package com.mukiva.usecase

sealed class ApiResult<TResult> {
    data class Success<TResult>(val data: TResult) : ApiResult<TResult>()
    data class Error<TResult>(val err: ApiError) : ApiResult<TResult>()
}