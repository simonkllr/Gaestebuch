package com.example.aufgabe3.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aufgabe3.model.BookingEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class SharedViewModel: ViewModel() {
    private val _bookingsEntries = MutableStateFlow<List<BookingEntry>>(emptyList())
    val bookingsEntries: StateFlow<List<BookingEntry>> = _bookingsEntries

    fun addBookingEntry(name: String, arrivalDate: LocalDate, departureDate: LocalDate){
        // TODO create a new booking entry and save it
        val newBookingEntry = BookingEntry( arrivalDate, departureDate, name)
        val currentList = _bookingsEntries.value.toMutableList()
        currentList.add(newBookingEntry)
        _bookingsEntries.value = currentList

    }

    fun deleteBookingEntry(entry: BookingEntry){
        // TODO delete a new booking entry

        _bookingsEntries.value -= entry
        /**  TODO überprüfen::::::::::::::::::::::::::::::::::::::
        val currentList = _bookingsEntries.value.toMutableList()
        currentList.removeAt(0)
        _bookingsEntries.value = currentList
*/
    }
}