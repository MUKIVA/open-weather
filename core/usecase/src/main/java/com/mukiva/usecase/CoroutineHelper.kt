package com.mukiva.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

object CoroutineHelper {
    @JvmStatic
    suspend fun <T> asyncWithDispatcher(dispatcher: CoroutineDispatcher, block: suspend() -> T): T {
        return CoroutineScope(Job() + dispatcher).async {
            block()
        }.await()
    }

    @JvmStatic
    suspend fun <T> doIO(block: suspend() -> T): T {
        return asyncWithDispatcher(Dispatchers.IO, block)
    }
}