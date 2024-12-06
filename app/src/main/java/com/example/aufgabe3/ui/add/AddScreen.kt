package com.example.aufgabe3.ui.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.DatePickerDialog
import java.time.Instant
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    var name by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf<LocalDate?>(null) }
    var departureDate by remember { mutableStateOf<LocalDate?>(null) }

    var showDateRangePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Booking Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (arrivalDate != null && departureDate != null) {
                    "${arrivalDate!!.format(dateFormatter)} - ${departureDate!!.format(dateFormatter)}"
                } else {
                    ""
                },
                onValueChange = {},
                label = { Text("Select Date Range") },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker = true },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            var showDialog by remember { mutableStateOf(false) }
            var dialogTitle by remember { mutableStateOf("") }
            var dialogMessage by remember { mutableStateOf("") }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = dialogTitle) },
                    text = { Text(text = dialogMessage) },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            Button(onClick = {
                // TODO Error handling and creating new BookingEntry
                when {
                    name.isEmpty() -> {
                        dialogTitle = "Fehlender Name"
                        dialogMessage = "Bitte geben Sie einen Namen ein."
                        showDialog = true
                    }

                    arrivalDate == null -> {
                        dialogTitle = "Fehlendes Ankunftsdatum"
                        dialogMessage = "Bitte wählen Sie ein Ankunftsdatum aus."
                        showDialog = true
                    }

                    departureDate == null -> {
                        dialogTitle = "Fehlendes Abreisedatum"
                        dialogMessage = "Bitte wählen Sie ein Abreisedatum aus."
                        showDialog = true
                    }

                    else -> {
                        sharedViewModel.addBookingEntry(name, arrivalDate!!, departureDate!!)
                        navController.popBackStack()
                    }
                }
            }) {
                Text("Speichern")
            }

        }
    }

    // TODO implement DateRangePicker Dialog logic
    if (showDateRangePicker) { // Zeigt den DateRangePickerModal an, wenn aktiviert
        DateRangePickerModal(
            onDateRangeSelected = { dateRange ->
                val (startMillis, endMillis) = dateRange
                if (startMillis != null && endMillis != null) {
                    arrivalDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    departureDate =
                        Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate()

                }
                showDateRangePicker = false // Schließt den Dialog
            },
            onDismiss = { showDateRangePicker = false } // Schließt den Dialog bei Abbrechen
        )
    }
}

@Composable
@Suppress("EXPERIMENTAL_API_USAGE")
@OptIn(ExperimentalMaterial3Api::class)
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}
