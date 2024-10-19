package com.learning.flightsearch.ui.screens.FlightSearch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.learning.flightsearch.R
import com.learning.flightsearch.ui.viewModels.Flight

@Composable
fun FlightsList(
    flights: List<Flight>,
    switchFavouriteStatus: (flight: Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        items(flights) {
            FlightItem(
                flight = it,
                onFavouritePress = { switchFavouriteStatus(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FlightItem(flight: Flight, onFavouritePress: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = stringResource(R.string.depart))
                Row {
                    Text(
                        text = flight.departFrom.iataCode,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flight.departFrom.name, style = TextStyle.Default)
                }

                Text(text = stringResource(R.string.arrive))
                Row {
                    Text(
                        text = flight.arriveTo.iataCode,
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flight.arriveTo.name, style = TextStyle.Default)
                }
            }
            IconButton(
                onClick = onFavouritePress,
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (flight.isFavourite) {
                        Icon(
                            Icons.Outlined.Favorite,
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                        )
                    }
                }

            }
        }
    }
}