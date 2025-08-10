package com.shah.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat

/**
 * Created by Monil on 10/08/25.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTopBar(currency: NumberFormat, total: Long) {
    TopAppBar(
        title = { Text("Add Expense") },
        actions = {
            Text(
                text = "Today: ${currency.format(total / 100.0)}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    )
}
