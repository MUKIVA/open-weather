//package com.mukiva.location_store
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.Query
//
//@Dao
//interface LocationPointDAO {
//
//    @Query("SELECT * FROM location")
//    fun getAll(): List<Location>
//
//    @Query("SELECT * FROM location WHERE uid LIKE :locationId LIMIT 1")
//    fun getById(locationId: Int): Location
//
//    @Query("SELECT * FROM location WHERE city_name LIKE :cityName LIMIT 1")
//    fun getByLocationName(cityName: String): List<Location>
//
//    @Insert
//    fun insert(vararg location: Location)
//
//    @Delete
//    fun delete(location: Location)
//}