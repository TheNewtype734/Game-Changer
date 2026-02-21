package com.squires.gamechanger.teamdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.TeamDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (uiState is UiState.Success) {
                        Text((uiState as UiState.Success).data.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        TeamDetailContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun TeamDetailContent(
    uiState: TeamDetailUiState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator()

            is UiState.Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            is UiState.Success -> {
                TeamDetailBody(
                    teamDetail = uiState.data,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun TeamDetailBody(
    teamDetail: TeamDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        // Banner image
        teamDetail.bannerUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Badge + name header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AsyncImage(
                    model = teamDetail.badgeUrl,
                    contentDescription = teamDetail.name,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit,
                )
                Column {
                    Text(
                        text = teamDetail.name,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    teamDetail.leagueName.let { league ->
                        Text(
                            text = league,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Details
            DetailRow(label = "Sport", value = teamDetail.sport)
            DetailRow(label = "Country", value = teamDetail.country)
            DetailRow(label = "Founded", value = teamDetail.formedYear)
            DetailRow(label = "Stadium", value = teamDetail.stadium)
            DetailRow(label = "Location", value = teamDetail.stadiumLocation)
            teamDetail.stadiumCapacity?.let { cap ->
                if (cap.isNotBlank()) DetailRow(label = "Capacity", value = cap)
            }
            DetailRow(label = "Website", value = teamDetail.website)

            teamDetail.description?.let { desc ->
                if (desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String?) {
    if (value.isNullOrBlank()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
