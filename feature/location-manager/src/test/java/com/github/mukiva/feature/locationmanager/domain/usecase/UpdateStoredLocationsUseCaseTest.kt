package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class UpdateStoredLocationsUseCaseTest {

    private val mMockLocationRepository = mock<ILocationRepository>()

    @Test
    fun `should return Success`() = runBlocking {
        Mockito.`when`(mMockLocationRepository.updateLocations(any()))
            .thenReturn(RequestResult.Success(Unit))
        val useCase = UpdateStoredLocationsUseCase(mMockLocationRepository)

        val actual = useCase(emptyList()) as? RequestResult.Success

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `should return Error if fail transaction`() = runBlocking {
        Mockito.`when`(mMockLocationRepository.updateLocations(any()))
            .thenReturn(RequestResult.Error(cause = Exception()))

        val useCase = UpdateStoredLocationsUseCase(mMockLocationRepository)
        val actual = useCase(emptyList()) as? RequestResult.Error

        Assertions.assertNotNull(actual)
    }
}
