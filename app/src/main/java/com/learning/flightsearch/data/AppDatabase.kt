package com.learning.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learning.flightsearch.data.daos.AirportDao
import com.learning.flightsearch.data.daos.FavoriteDao
import com.learning.flightsearch.data.model.Airport
import com.learning.flightsearch.data.model.Favorite

@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun favouriteDao(): FavoriteDao

    companion object {
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "flight_db")
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}