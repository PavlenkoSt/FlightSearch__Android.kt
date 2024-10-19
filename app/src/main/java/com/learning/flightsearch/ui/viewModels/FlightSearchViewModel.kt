package com.learning.flightsearch.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.learning.flightsearch.data.AppDatabase
import com.learning.flightsearch.data.model.Airport
import com.learning.flightsearch.data.model.Favorite
import com.learning.flightsearch.data.repositories.AirportRepository
import com.learning.flightsearch.data.repositories.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DropdownSuggestion(
    val text: String,
    val onClick: () -> Unit
)

data class Flight(
    val departFrom: Airport,
    val arriveTo: Airport,
    val isFavourite: Boolean
)

class FlightSearchViewModel(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private var airports: List<Airport> = listOf()
    private var favorites: List<Favorite> = listOf()

    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            airportRepository.getAllAirports().collectLatest {
                airports = it

                if (_uiState.value.activeAirport != null) {
                    generateFlights(_uiState.value.activeAirport!!)
                }

                generateFavoriteFlights()
            }
        }
        viewModelScope.launch {
            favoriteRepository.getAll().collectLatest {
                favorites = it

                if (_uiState.value.activeAirport != null) {
                    generateFlights(_uiState.value.activeAirport!!)
                }

                generateFavoriteFlights()
            }
        }
    }

    private fun onSuggestionClick(airport: Airport) {
        onSuggestionsMenuSwitchState(false)
        _uiState.update { state ->
            state.copy(
                activeAirport = airport,
                query = airport.name
            )
        }
        generateFlights(airport)
    }

    private fun getAirportsSuggestions(query: String): List<DropdownSuggestion> {
        return airports
            .filter { airport ->
                airport.name.contains(query, ignoreCase = true)
            }
            .map { airport ->
                DropdownSuggestion(text = airport.name, onClick = {
                    onSuggestionClick(airport)
                })
            }
    }

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                suggestions = getAirportsSuggestions(query)
            )
        }
    }

    fun onSuggestionsMenuSwitchState(status: Boolean) {
        _uiState.update {
            it.copy(
                suggestionsMenuExpanded = status
            )
        }
    }

    fun clearActiveAirport() {
        _uiState.update {
            it.copy(activeAirport = null)
        }
    }

    fun switchFavouriteStatus(flight: Flight) {
        val favouriteItem = Favorite(
            departureCode = flight.departFrom.iataCode,
            destinationCode = flight.arriveTo.iataCode
        )

        viewModelScope.launch {
            if (flight.isFavourite) {
                favoriteRepository.removeFromFavourites(favouriteItem)
            } else {
                favoriteRepository.addToFavourites(favouriteItem)
            }
        }
    }

    private fun generateFavoriteFlights() {
        if (airports.isEmpty()) return

        val favoriteFlightList: MutableList<Flight> = mutableListOf()

        favorites.forEach {
            val departFrom = airports.find { airport -> airport.iataCode == it.departureCode }
            val arriveTo = airports.find { airport -> airport.iataCode == it.destinationCode }

            if (departFrom != null && arriveTo != null) {
                favoriteFlightList.add(
                    Flight(
                        departFrom = departFrom,
                        arriveTo = arriveTo,
                        isFavourite = true
                    )
                )
            }
        }

        _uiState.update {
            it.copy(favoriteFlightList = favoriteFlightList)
        }
    }

    private fun generateFlights(activeAirport: Airport) {
        val otherAirports = airports.filter { airport -> airport.id != activeAirport.id }

        val flightList = otherAirports.map { airport ->
            val isFavourite = favorites.find { favourite ->
                favourite.departureCode == activeAirport.iataCode &&
                        favourite.destinationCode == airport.iataCode
            } != null

            Flight(
                departFrom = activeAirport,
                arriveTo = airport,
                isFavourite = isFavourite
            )
        }

        _uiState.update {
            it.copy(flightList = flightList)
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val db = AppDatabase.getDatabase(context)
                val favoriteRepository = FavoriteRepository(favouriteDao = db.favouriteDao())
                val airportRepository = AirportRepository(airportDao = db.airportDao())

                FlightSearchViewModel(
                    favoriteRepository = favoriteRepository,
                    airportRepository = airportRepository
                )
            }
        }
    }
}

data class FlightSearchUiState(
    var query: String = "",
    var suggestionsMenuExpanded: Boolean = false,
    var suggestions: List<DropdownSuggestion> = emptyList(),
    var activeAirport: Airport? = null,
    var flightList: List<Flight> = emptyList(),
    var favoriteFlightList: List<Flight> = emptyList()
)