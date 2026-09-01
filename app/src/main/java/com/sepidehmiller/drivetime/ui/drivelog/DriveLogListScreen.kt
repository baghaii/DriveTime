package com.sepidehmiller.drivetime.ui.drivelog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sepidehmiller.drivetime.R
import com.sepidehmiller.drivetime.data.source.DriveTimeUi
import com.sepidehmiller.drivetime.ui.theme.DriveTimeTheme
import com.sepidehmiller.drivetime.ui.theme.bodyLargeEmphasized

@Composable
fun DriveLogScreen(
    viewModel: DriveLogListViewModel = hiltViewModel(),
    seeDetailsAction: (id: Int) -> Unit,
    fabAction: () -> Unit
) {
    val state by viewModel.driveTimes.collectAsStateWithLifecycle(DriveTimeState.Loading)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FAB(fabAction = fabAction ) }
    ) { innerPadding ->
        when (state) {
            DriveTimeState.Loading -> {
                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            DriveTimeState.Empty -> {
                EmptyState(innerPadding)
            }
            else -> DriveLogList(
                modifier = Modifier.padding(innerPadding),
                driveTimeState = state as DriveTimeState.Loaded,
                seeDetailsAction = seeDetailsAction
            )
        }
    }
}

@Composable
fun FAB(
    fabAction: () -> Unit
) {
    FloatingActionButton(
        onClick = fabAction,
        modifier = Modifier.padding(16.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = "GO",
            style = MaterialTheme.typography.bodyLargeEmphasized
        )
    }
}

@Composable
fun DriveLogList(
    modifier: Modifier = Modifier,
    driveTimeState: DriveTimeState.Loaded,
    seeDetailsAction: (Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            DriveTimeWithIcon(icon = R.drawable.sun, text = stringResource(R.string.day_hours, driveTimeState.daySum))
            Spacer(modifier = Modifier.height(8.dp))
            DriveTimeWithIcon(icon = R.drawable.moon, text = stringResource(R.string.night_hours, driveTimeState.nightSum))
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(driveTimeState.driveTimes.size) { index ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            DriveLogListItem(
                driveTime = driveTimeState.driveTimes[index],
                seeDetailsAction = seeDetailsAction
            )
        }
    }
}

@Composable
fun DriveLogListItem(
    driveTime: DriveTimeUi,
    seeDetailsAction: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        onClick = { seeDetailsAction(driveTime.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                style = MaterialTheme.typography.bodyLargeEmphasized,
                text = driveTime.date
            )
            Spacer(modifier = Modifier.height(16.dp))
            DriveTimeWithIcon(
                icon = R.drawable.sun,
                text =
                    pluralStringResource(R.plurals.hours, driveTime.dayHours.toInt(), driveTime.dayHours.toInt())  + " " +
                    pluralStringResource(R.plurals.minutes, driveTime.dayMinutes.toInt(), driveTime.dayMinutes.toInt())
            )
            Spacer(modifier = Modifier.height(8.dp))
            DriveTimeWithIcon(
                icon = R.drawable.moon,
                text =  pluralStringResource(R.plurals.hours, driveTime.nightHours.toInt(), driveTime.nightHours.toInt())  + " " +
                        pluralStringResource(R.plurals.minutes, driveTime.nightMinutes.toInt(), driveTime.nightMinutes.toInt())
            )
        }
    }
}

@Composable
fun DriveTimeWithIcon(@DrawableRes icon: Int, text: String) {
    Row {
        Image(
            modifier = Modifier.width(24.dp).height(24.dp),
            painter = painterResource(icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun EmptyState(innerPadding: PaddingValues) {
    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(320.dp)
                .height(100.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.auto2_lt_blue_400px),
            contentDescription = stringResource(R.string.no_drives_logged),
        )
        Text(
            text = stringResource(R.string.lets_get_driving),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DriveTimeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = { FAB({}) }
        ) { innerPadding ->
            EmptyState(innerPadding)
        }
    }
}

