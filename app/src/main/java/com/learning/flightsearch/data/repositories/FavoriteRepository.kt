package com.learning.flightsearch.data.repositories

import com.learning.flightsearch.data.daos.FavoriteDao
import com.learning.flightsearch.data.model.Favorite
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favouriteDao: FavoriteDao) {
    fun getAll(): Flow<List<Favorite>> {
        return favouriteDao.getAll()
    }

    suspend fun addToFavourites(item: Favorite) {
        return favouriteDao.insert(item)
    }

    suspend fun removeFromFavourites(item: Favorite) {
        return favouriteDao.delete(
            destinationCode = item.destinationCode,
            departureCode = item.departureCode
        )
    }
}