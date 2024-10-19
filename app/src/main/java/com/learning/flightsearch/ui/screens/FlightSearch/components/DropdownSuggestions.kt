package com.learning.flightsearch.ui.screens.FlightSearch.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.learning.flightsearch.R
import com.learning.flightsearch.ui.viewModels.DropdownSuggestion
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun DropdownSuggestions(
    suggestions: List<DropdownSuggestion>,
    onOptionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        if (suggestions.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(stringResource(R.string.airports_not_found))
            }
        } else {
            val listState = rememberLazyListState()

            LazyColumnScrollbar(
                state = listState,
                settings = ScrollbarSettings.Default.copy(
                    alwaysShowScrollbar = true,
                    thumbUnselectedColor = Color(0x80FFFFFF),
                    thumbSelectedColor = Color.White
                ),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .animateContentSize()
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    state = listState
                ) {
                    items(suggestions) {
                        DropdownSuggestionItem(suggestion = it, onOptionClicked = onOptionClicked)
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownSuggestionItem(suggestion: DropdownSuggestion, onOptionClicked: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            suggestion.onClick()
            onOptionClicked()
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
            Text(text = suggestion.text, style = TextStyle.Default)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}