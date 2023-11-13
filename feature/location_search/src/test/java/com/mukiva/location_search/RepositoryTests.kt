package com.mukiva.location_search

import com.mukiva.location_search.data.LocationLocalEntity
import com.mukiva.location_search.domain.mapper.LocationMapper
import com.mukiva.location_search.domain.model.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class MappingTests {

    private val mMapper = LocationMapper

    @Test
    fun LocalToDomainTest() {
        val local = LocationLocalEntity(100, "test", "test", "test")
        val actual = with(mMapper) { local.asDomain() }
        val expected = Location(100, "test", "test", "test", true)
        assertEquals(expected, actual)
    }
}