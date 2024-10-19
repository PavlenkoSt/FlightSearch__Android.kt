package com.learning.flightsearch.ui.screens.FlightSearch

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learning.flightsearch.R
import com.learning.flightsearch.ui.screens.FlightSearch.components.DropdownSuggestions
import com.learning.flightsearch.ui.screens.FlightSearch.components.FlightsList
import com.learning.flightsearch.ui.screens.FlightSearch.components.TopBar
import com.learning.flightsearch.ui.viewModels.FlightSearchViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FlightSearchScreen() {
    val flightSearchViewModel: FlightSearchViewModel =
        viewModel(factory = FlightSearchViewModel.provideFactory(context = LocalContext.current))
    val uiState by flightSearchViewModel.uiState.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun applyQuery() {
        val isEmpty = uiState.query.trim() == ""

        if (isEmpty) {
            flightSearchViewModel.clearActiveAirport()
        }

        val queryToSet = if (isEmpty) ""
        else uiState.activeAirport?.name ?: ""

        flightSearchViewModel.onQueryChange(queryToSet)
        flightSearchViewModel.onSuggestionsMenuSwitchState(false)
        focusManager.clearFocus()
    }

    Scaffold(topBar = {
        TopBar(
            query = uiState.query,
            onQueryChange = {
                flightSearchViewModel.onQueryChange(it)
                flightSearchViewModel.onSuggestionsMenuSwitchState(true)
            },
            onClear = {
                flightSearchViewModel.onQueryChange("")
                flightSearchViewModel.onSuggestionsMenuSwitchState(false)
                flightSearchViewModel.clearActiveAirport()
                focusManager.clearFocus()
            },
            applyQuery = { applyQuery() },
            focusRequester = focusRequester,
        )
    }, modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = { applyQuery() })
        }) { paddingValue ->
        Column(modifier = Modifier.padding(paddingValue)) {
            AnimatedVisibility(
                visible = !uiState.suggestionsMenuExpanded && uiState.favoriteFlightList.isNotEmpty() && uiState.activeAirport == null
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.favourite_routes), style = TextStyle.Default.copy(
                            fontWeight = FontWeight.Bold, fontSize = 20.sp
                        ), modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    FlightsList(flights = uiState.favoriteFlightList,
                        switchFavouriteStatus = { flightSearchViewModel.switchFavouriteStatus(it) })
                }
            }
            AnimatedVisibility(visible = uiState.suggestionsMenuExpanded && uiState.query.isNotEmpty()) {
                DropdownSuggestions(suggestions = uiState.suggestions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .heightIn(0.dp, 300.dp),
                    onOptionClicked = { focusManager.clearFocus() })
            }
            AnimatedVisibility(visible = uiState.activeAirport != null && !uiState.suggestionsMenuExpanded) {
                FlightsList(flights = uiState.flightList,
                    switchFavouriteStatus = { flightSearchViewModel.switchFavouriteStatus(it) })
            }
        }
    }
}


@Preview
@Composable
fun FlightSearchPreview() {
    FlightSearchScreen()
}