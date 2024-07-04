package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any


class AddLocationUseCaseTest {

    private val mMockLocationRepository = mock<ILocationRepository>()
    private val mDomainLocation = createLocationMock()

    private fun createLocationMock(): Location {
        return Location(
            id = 0,
            position = 0,
            cityName = "Some city",
            regionName = "Some region",
            countryName = "Country name",
            isAdded = false,
        )
    }

    @Test
    fun `Should return the RequestResult Success`(): Unit = runBlocking {
        Mockito.`when`(mMockLocationRepository.addLocalLocation(any()))
            .thenReturn(RequestResult.Success(Unit))

        val useCase = AddLocationUseCase(mMockLocationRepository)
        val actual = useCase(mDomainLocation)
        println(actual)
        Assertions.assertTrue(actual is RequestResult.Success)
    }

    @Test
    fun `Should return the RequestResult Error`(): Unit = runBlocking {
        Mockito.`when`(mMockLocationRepository.addLocalLocation(any()))
            .thenReturn(RequestResult.Error(null, Exception()))

        val useCase = AddLocationUseCase(mMockLocationRepository)
        val actual = useCase(mDomainLocation)
        println(actual)
        Assertions.assertTrue(actual is RequestResult.Error)
    }
}
