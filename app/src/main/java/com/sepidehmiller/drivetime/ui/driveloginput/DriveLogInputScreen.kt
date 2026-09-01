package com.sepidehmiller.drivetime.ui.driveloginput

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sepidehmiller.drivetime.utils.AppDateFormatter
import java.time.Instant
import java.time.ZoneOffset


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveLogInputScreen(
    viewModel: DriveLogInputViewModel = hiltViewModel(),
    closeAction: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton (onClick = closeAction) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }},
                title = { Text("Drive Log Input") }
             )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            var showPicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = Instant.now().toEpochMilli()
            )
            val dateTextFieldState = rememberTextFieldState(
                initialText = AppDateFormatter.formatMillis(Instant.now().toEpochMilli())
            )
            val commentsTextFieldState = rememberTextFieldState()

            val dayTimeHoursTextFieldState = rememberTextFieldState()
            val dayTimeMinutesTextFieldState = rememberTextFieldState()
            val nightTimeHoursTextFieldState = rememberTextFieldState()
            val nightTimeMinutesTextFieldState = rememberTextFieldState()

            if(showPicker) {
                DatePickerDialog(
                    onDismissRequest = { showPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatted = AppDateFormatter.formatMillis(millis, ZoneOffset.UTC)
                                dateTextFieldState.edit {
                                    replace(0, length, formatted)
                                }
                            }
                            showPicker = false
                        }) {
                            Text("OK")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            OutlinedTextField(
                state = dateTextFieldState,
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showPicker = true }) {
                        Icon(Icons.Filled.DateRange, contentDescription = null)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Day Time",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                NumericTextField(
                    state = dayTimeHoursTextFieldState,
                    label = "Hours"
                )
                NumericTextField(
                    state = dayTimeMinutesTextFieldState,
                    label = "Minutes"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Night Time",
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                NumericTextField(
                    state = nightTimeHoursTextFieldState,
                    label = "Hours"
                )
                NumericTextField(
                    state = nightTimeMinutesTextFieldState,
                    label = "Minutes"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text (
                text = "Comments"
            )
            BasicTextField(
                state = commentsTextFieldState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.align(Alignment.End),
                enabled = (dayTimeHoursTextFieldState.text.isDigitsOnly() || dayTimeHoursTextFieldState.text.isEmpty()) &&
                        (dayTimeMinutesTextFieldState.text.isDigitsOnly() || dayTimeMinutesTextFieldState.text.isEmpty()) &&
                        (nightTimeHoursTextFieldState.text.isDigitsOnly() || nightTimeHoursTextFieldState.text.isEmpty()) &&
                        (nightTimeMinutesTextFieldState.text.isDigitsOnly() || nightTimeMinutesTextFieldState.text.isEmpty()),
                onClick = {
                    viewModel.addDriveLog(
                        dateString = dateTextFieldState.text.toString(),
                        dayHoursString = dayTimeHoursTextFieldState.text.toString(),
                        dayMinutesString = dayTimeMinutesTextFieldState.text.toString(),
                        nightHoursString = nightTimeHoursTextFieldState.text.toString(),
                        nightMinutesString = nightTimeMinutesTextFieldState.text.toString(),
                        comments = commentsTextFieldState.text.toString()
                    )
                    closeAction()
                }
            ) {
                Text("Log Drive Time")
            }
        }
    }
}

@Composable
fun NumericTextField(
    state: TextFieldState,
    label: String
) {
    Column {
        Text(label)
        var isFocused by remember { mutableStateOf (false) }
        BasicTextField(
            state = state,
            modifier = Modifier
                .onFocusChanged {
                    isFocused = it.hasFocus
                }
                .border(
                    width = if (isFocused) 2.dp else 1.dp,
                    color = when {
                        !state.text.isDigitsOnly() -> MaterialTheme.colorScheme.error
                        (isFocused || state.text.isDigitsOnly()) ->
                            MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline
                    },
                    shape = MaterialTheme.shapes.extraSmall
                )
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        if (!state.text.isDigitsOnly()) {
            Text(
                text = "Numeric values only!",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
