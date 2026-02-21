package com.squires.gamechanger.leagues

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.squires.gamechanger.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen(
    onLeagueClick: (leagueName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaguesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Leagues") },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        LeaguesContent(
            uiState = uiState,
            onLeagueClick = onLeagueClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun LeaguesContent(
    uiState: LeagueUiState,
    onLeagueClick: (leagueName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            is UiState.Success -> {
                if (uiState.data.isEmpty()) {
                    Text(
                        text = "No leagues found",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(
                            items = uiState.data,
                            key = { it.id },
                        ) { league ->
                            LeagueCard(
                                league = league,
                                onClick = { onLeagueClick(league.name) },
                                modifier = Modifier.padding(bottom = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
