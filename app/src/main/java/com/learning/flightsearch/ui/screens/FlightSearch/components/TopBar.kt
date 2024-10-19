package com.learning.flightsearch.ui.screens.FlightSearch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.learning.flightsearch.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    query: String,
    onQueryChange: (query: String) -> Unit,
    applyQuery: () -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_title)) },
            colors = TopAppBarDefaults.topAppBarColors()
                .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { onQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = CircleShape)
                    .focusRequester(focusRequester),
                shape = CircleShape,
                placeholder = { Text(text = stringResource(R.string.search_flight_input_placeholder)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = null
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onClear() }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                keyboardActions = KeyboardActions(onDone = { applyQuery() })
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
