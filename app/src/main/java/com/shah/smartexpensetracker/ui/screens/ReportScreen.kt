package com.shah.smartexpensetracker.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shah.smartexpensetracker.ui.components.CategoryBars
import com.shah.smartexpensetracker.ui.components.DailyBarChart
import com.shah.smartexpensetracker.ui.viewmodels.ReportViewModel
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Created by Monil on 09/08/25.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(viewModel: ReportViewModel = hiltViewModel()) {
    val currency = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }
    val dayFmt = remember { DateTimeFormatter.ofPattern("EEE d") }
    var menuOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val ui by viewModel.formState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.share.collect { uri ->
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(share, "Share expenses CSV"))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Report (Last 7 days)")
                        Text(
                            "${ui.rangeStart} → ${ui.rangeEnd}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    Text(
                        "Total: ${currency.format(ui.grandTotalCents / 100.0)}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        DropdownMenuItem(text = { Text("Export CSV (This day)") }, onClick = { menuOpen = false; viewModel.exportCurrentDay() })
                        DropdownMenuItem(text = { Text("Export CSV (Last 7 days)") }, onClick = { menuOpen = false; viewModel.exportLast7Days() })
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("Daily totals", style = MaterialTheme.typography.titleMedium)
            DailyBarChart(
                data = ui.daily.map { it.totalCents.toFloat() / 100f },
                labels = ui.daily.map { dayFmt.format(it.date) },
                valueFormatter = { v -> currency.format(v.toDouble()) }
            )

            HorizontalDivider()

            Text("By category", style = MaterialTheme.typography.titleMedium)
            CategoryBars(
                data = ui.byCategory.map { it.totalCents.toFloat() / 100f },
                labels = ui.byCategory.map {
                    it.category.name.lowercase().replaceFirstChar { c -> c.titlecase() }
                },
                valueFormatter = { v -> currency.format(v.toDouble()) }
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}
