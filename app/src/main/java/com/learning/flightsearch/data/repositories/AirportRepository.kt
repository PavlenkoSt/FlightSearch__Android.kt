package com.learning.flightsearch.data.repositories

import com.learning.flightsearch.data.daos.AirportDao
import com.learning.flightsearch.data.model.Airport
import kotlinx.coroutines.flow.Flow

class AirportRepository (
    private val airportDao: AirportDao
) {
    fun getAllAirports(): Flow<List<Airport>> {
        return airportDao.getAll()
    }
}