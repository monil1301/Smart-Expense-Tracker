package com.shah.smartexpensetracker.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.shah.smartexpensetracker.data.model.Expense

/**
 * Created by Monil on 10/08/25.
 */

@Composable
fun ExpenseRow(e: Expense, currency: java.text.NumberFormat) {
    ListItem(
        headlineContent = { Text(e.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = {
            Text(e.category.name.lowercase().replaceFirstChar { it.titlecase() })
        },
        trailingContent = {
            Text(currency.format(e.amountCents / 100.0), style = MaterialTheme.typography.titleMedium)
        }
    )
    HorizontalDivider()
}
