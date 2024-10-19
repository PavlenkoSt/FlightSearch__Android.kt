package com.learning.flightsearch.data.daos

import androidx.room.Dao
import androidx.room.Query
import com.learning.flightsearch.data.model.Airport
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * from airport")
    fun getAll(): Flow<List<Airport>>
}