package com.sepidehmiller.drivetime.ui.drivelogdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sepidehmiller.drivetime.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogDetailScreen(
    viewModel: DriveLogDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Drive Log Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (val state = uiState) {
                DriveTimeDetailUiState.Loading -> CircularProgressIndicator()
                is DriveTimeDetailUiState.DriveTimeDetailUi -> {
                    Text(text = state.date, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Day Hours", style = MaterialTheme.typography.titleMedium)
                    Text(text = pluralStringResource(R.plurals.hours, state.dayHours.toInt(), state.dayHours.toInt()) + " " +
                        pluralStringResource(R.plurals.minutes, state.dayMinutes.toInt(), state.dayMinutes.toInt()), style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Night Hours", style = MaterialTheme.typography.titleMedium)
                    Text(text = pluralStringResource(R.plurals.hours, state.nightHours.toInt(), state.nightHours.toInt()) + " " +
                            pluralStringResource(R.plurals.minutes, state.nightMinutes.toInt(), state.nightMinutes.toInt()), style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Comments:", style = MaterialTheme.typography.titleMedium)
                    Text(text = state.comments, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
