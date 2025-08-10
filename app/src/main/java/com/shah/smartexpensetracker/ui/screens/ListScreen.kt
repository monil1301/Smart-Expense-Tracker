package com.shah.smartexpensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shah.smartexpensetracker.ui.components.DatePickerSheet
import com.shah.smartexpensetracker.ui.components.EmptyState
import com.shah.smartexpensetracker.ui.components.ExpenseRow
import com.shah.smartexpensetracker.ui.components.FilterToggle
import com.shah.smartexpensetracker.ui.viewmodels.ExpenseListViewModel
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Created by Monil on 09/08/25.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }
    val dateFmt = remember { DateTimeFormatter.ofPattern("EEE, d MMM yyyy") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Expenses")
                        Text(
                            text = dateFmt.format(formState.date),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    // Header stats
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            "Total: ${currency.format(formState.totalCents / 100.0)}",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            "${formState.totalCount} items",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            // Filters row
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalIconButton(onClick = viewModel::prevDay) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Previous day"
                    )
                }
                OutlinedButton(onClick = { viewModel.openDatePicker(true) }) {
                    Icon(Icons.Default.Today, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Pick date")
                }
                FilledTonalIconButton(onClick = viewModel::nextDay) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Next day"
                    )
                }

                Spacer(Modifier.weight(1f))

                FilterToggle(grouping = formState.grouping, onToggle = viewModel::toggleGrouping)
            }

            if (formState.items.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    formState.sections.forEach { section ->
                        item {
                            Text(
                                section.title,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(section.expenses) { e ->
                            ExpenseRow(e, currency)
                        }
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }

        // Optional Material3 DatePicker (kept lightweight)
        if (formState.isDatePickerOpen) {
            DatePickerSheet(
                current = formState.date,
                onPicked = { viewModel.setDate(it); viewModel.openDatePicker(false) },
                onDismiss = { viewModel.openDatePicker(false) }
            )
        }
    }
}
