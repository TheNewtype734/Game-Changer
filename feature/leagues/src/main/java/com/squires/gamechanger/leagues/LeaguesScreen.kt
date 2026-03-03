package com.squires.gamechanger.leagues

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaguesScreen(
    onLeagueClick: (leagueName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LeaguesViewModel = hiltViewModel(),
) {
    val lazyLeagues = viewModel.pagedLeagues.collectAsLazyPagingItems()
    val refreshState by viewModel.refreshState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Leagues") },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        LeaguesContent(
            lazyLeagues = lazyLeagues,
            refreshState = refreshState,
            searchQuery = searchQuery,
            searchState = searchState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onLeagueClick = onLeagueClick,
            onRetry = viewModel::retry,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun LeaguesContent(
    lazyLeagues: LazyPagingItems<League>,
    refreshState: Result<Unit>,
    searchQuery: String,
    searchState: LeaguesUiState,
    onSearchQueryChange: (String) -> Unit,
    onLeagueClick: (leagueName: String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search by country") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            },
            singleLine = true,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (searchQuery.isBlank()) {
                BrowseLeaguesContent(
                    lazyLeagues = lazyLeagues,
                    refreshState = refreshState,
                    onLeagueClick = onLeagueClick,
                    onRetry = onRetry,
                )
            } else {
                SearchLeaguesContent(
                    searchState = searchState,
                    searchQuery = searchQuery,
                    onLeagueClick = onLeagueClick,
                    onRetry = onRetry,
                )
            }
        }
    }
}

@Composable
private fun BrowseLeaguesContent(
    lazyLeagues: LazyPagingItems<League>,
    refreshState: Result<Unit>,
    onLeagueClick: (leagueName: String) -> Unit,
    onRetry: () -> Unit,
) {
    if (lazyLeagues.itemCount > 0) {
        Box(modifier = Modifier.fillMaxSize()) {
            PagedLeaguesList(
                lazyLeagues = lazyLeagues,
                onLeagueClick = onLeagueClick,
            )
            if (refreshState is Result.Error) {
                ErrorBanner(
                    message = refreshState.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    } else {
        when (refreshState) {
            is Result.Loading -> CircularProgressIndicator()
            is Result.Error -> FullScreenError(message = refreshState.message, onRetry = onRetry)
            is Result.Success -> Text(
                text = "No leagues found",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SearchLeaguesContent(
    searchState: LeaguesUiState,
    searchQuery: String,
    onLeagueClick: (leagueName: String) -> Unit,
    onRetry: () -> Unit,
) {
    when (searchState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Error -> FullScreenError(message = searchState.message, onRetry = onRetry)
        is UiState.Success -> {
            if (searchState.data.isEmpty()) {
                Text(
                    text = "No leagues match \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                LeaguesList(
                    leagues = searchState.data,
                    onLeagueClick = onLeagueClick,
                )
            }
        }
    }
}

@Composable
private fun PagedLeaguesList(
    lazyLeagues: LazyPagingItems<League>,
    onLeagueClick: (leagueName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            count = lazyLeagues.itemCount,
            key = lazyLeagues.itemKey { it.id },
        ) { index ->
            val league = lazyLeagues[index] ?: return@items
            LeagueCard(
                league = league,
                onClick = { onLeagueClick(league.name) },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Composable
private fun LeaguesList(
    leagues: List<League>,
    onLeagueClick: (leagueName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = leagues,
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

@Composable
private fun FullScreenError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onRetry) {
                Text(
                    text = "Retry",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}
