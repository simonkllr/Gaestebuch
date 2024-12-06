package com.example.aufgabe3.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aufgabe3.model.BookingEntry
import com.example.aufgabe3.viewmodel.SharedViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val bookingsEntries by sharedViewModel.bookingsEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Entries") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add booking")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {


            // TODO inform the user if no bookingsEntries otherwise LazyColumn for bookingsEntries
            if (bookingsEntries.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, // Beispiel-Icon
                        contentDescription = "No bookings",
                        modifier = Modifier.size(44.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Abstand zwischen Icon und Text
                    Text(
                        text = "No bookings yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bookingsEntries.size) { booking ->
                        BookingEntryItem(
                            booking = bookingsEntries[booking],
                            onDeleteClick = {
                                sharedViewModel.deleteBookingEntry(bookingsEntries[booking])
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingEntryItem(
    booking: BookingEntry,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    // TODO display booking name,
                    text = booking.name, // Name des Buchungseintrags
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                 // TODO display date in right format,
                    text = "${booking.arrivalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} - " +
                                "${booking.departureDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}", // Datum formatieren
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete booking")
            }
        }
    }
}
