package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetAddedLocationUseCaseTest {

    private val mMockLocationRepository = mock<ILocationRepository>()

    @Test
    fun `should return InProgress`(): Unit = runBlocking {
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.InProgress()))

        val useCase = GetAddedLocationsUseCase(mMockLocationRepository)
        val actual = useCase()
            .singleOrNull()
        Assertions.assertTrue(actual is RequestResult.InProgress)
    }

    @Test
    fun `should return Error`(): Unit = runBlocking {
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Error(null, Exception())))

        val useCase = GetAddedLocationsUseCase(mMockLocationRepository)
        val actual = useCase()
            .singleOrNull() as? RequestResult.Error
        println(actual)
        val cause = actual?.cause
        val data = actual?.data
        Assertions.assertNull(data)
        Assertions.assertNotNull(cause)
        Assertions.assertTrue(actual is RequestResult.Error)
    }
}
