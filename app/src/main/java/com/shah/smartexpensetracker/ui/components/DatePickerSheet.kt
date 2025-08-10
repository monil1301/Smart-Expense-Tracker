package com.shah.smartexpensetracker.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

/**
 * Created by Monil on 10/08/25.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSheet(
    current: java.time.LocalDate,
    onPicked: (java.time.LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = current.atStartOfDay(java.time.ZoneOffset.UTC).toInstant()
            .toEpochMilli()
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val millis = state.selectedDateMillis ?: return@TextButton
                val picked = java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneOffset.UTC)
                    .toLocalDate()
                onPicked(picked)
            }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    ) {
        DatePicker(state = state)
    }
}
