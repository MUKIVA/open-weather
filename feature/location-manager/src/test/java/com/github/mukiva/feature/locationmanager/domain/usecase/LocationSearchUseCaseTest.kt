package com.github.mukiva.feature.locationmanager.domain.usecase

import com.github.mukiva.feature.locationmanager.domain.model.Location
import com.github.mukiva.openweather.core.domain.settings.Lang
import com.github.mukiva.weatherdata.ILocationRepository
import com.github.mukiva.weatherdata.ISettingsRepository
import com.github.mukiva.weatherdata.utils.RequestResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import com.github.mukiva.weatherdata.models.Location as DataLocation

class LocationSearchUseCaseTest {

    private val mMockLocationRepository = mock<ILocationRepository>()
    private val mMockSettingsRepository = mock<ISettingsRepository>()
    private val mMockLocalDateTime = LocalDateTime(0, 1, 1, 0, 0, 0, 0)

    @Test
    fun `empty query should return empty list`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Success(emptyList())))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.Success(emptyList())))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.Success

        Assertions.assertNotNull(actual)
        Assertions.assertTrue(actual?.data?.isEmpty() ?: false)
    }

    @Test
    fun `if get local location throw exception`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Error(cause = Exception())))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.Success(emptyList())))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.Error

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `if search throw exception`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Success(emptyList())))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.Error(cause = Exception())))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.Error

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `getAllLocal return InProgress`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.InProgress()))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.Success(emptyList())))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.InProgress

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `searchRemote return InProgress`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Success(emptyList())))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.InProgress()))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.InProgress

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `all flow return InProgress`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.InProgress()))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(flowOf(RequestResult.InProgress()))

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.InProgress

        Assertions.assertNotNull(actual)
    }

    @Test
    fun `search return any elements`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(flowOf(RequestResult.Success(emptyList())))
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(
                flowOf(
                    RequestResult.Success(
                        listOf(
                            createMockDataLocation(0),
                            createMockDataLocation(1),
                            createMockDataLocation(2),
                        )
                    )
                )
            )

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val expectedData = listOf(
            createMockLocation(0),
            createMockLocation(1),
            createMockLocation(2)
        )
        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.Success

        Assertions.assertNotNull(actual)
        Assertions.assertTrue(
            actual?.data?.all { location ->
                location in expectedData
            } ?: false
        )
    }

    @Test
    fun `search return any elements and one element is already added`(): Unit = runBlocking {
        Mockito.`when`(mMockSettingsRepository.getLocalization())
            .thenReturn(flowOf(Lang.EN))
        Mockito.`when`(mMockLocationRepository.getAllLocal())
            .thenReturn(
                flowOf(
                    RequestResult.Success(
                        listOf(
                            createMockDataLocation(0)
                        )
                    )
                )
            )
        Mockito.`when`(mMockLocationRepository.searchRemote(any(), any()))
            .thenReturn(
                flowOf(
                    RequestResult.Success(
                        listOf(
                            createMockDataLocation(0),
                            createMockDataLocation(1),
                            createMockDataLocation(2),
                        )
                    )
                )
            )

        val useCase = LocationSearchUseCase(
            mMockLocationRepository,
            mMockSettingsRepository
        )

        val expectedData = listOf(
            createMockLocation(0, isAdded = true),
            createMockLocation(1),
            createMockLocation(2)
        )
        val actual = useCase.invoke("")
            .singleOrNull() as? RequestResult.Success

        Assertions.assertNotNull(actual)
        Assertions.assertTrue(
            actual?.data?.all { location ->
                location in expectedData
            } ?: false
        )
    }

    private fun createMockDataLocation(id: Long): DataLocation {
        return DataLocation(
            id = id,
            name = "",
            region = "",
            country = "",
            lat = 0.0,
            lon = 0.0,
            tzId = "",
            localtimeEpoch = mMockLocalDateTime,
            priority = 0,
        )
    }

    private fun createMockLocation(
        id: Int,
        isAdded: Boolean = false
    ): Location {
        return Location(
            id = id,
            position = 0,
            cityName = "",
            regionName = "",
            countryName = "",
            isAdded = isAdded,
        )
    }
}
