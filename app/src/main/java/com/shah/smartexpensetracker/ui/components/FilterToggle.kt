package com.shah.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.shah.smartexpensetracker.utils.enums.GroupingMode

/**
 * Created by Monil on 10/08/25.
 */

@Composable
fun FilterToggle(grouping: GroupingMode, onToggle: () -> Unit) {
    val chipLabels = listOf("Time", "Category")

    // Calculate max text width
    val textMeasurer = rememberTextMeasurer()
    val maxWidthPx = chipLabels.maxOf {
        textMeasurer.measure(it).size.width
    }
    val maxWidthDp = with(LocalDensity.current) { maxWidthPx.toDp() + 36.dp }

    Column(
        verticalArrangement = Arrangement.spacedBy((-8).dp) // reduced spacing
    ) {
        FilterChip(
            selected = grouping == GroupingMode.BY_TIME,
            onClick = { if (grouping != GroupingMode.BY_TIME) onToggle() },
            label = { Text("Time", maxLines = 1) },
            modifier = Modifier.width(maxWidthDp)
        )
        FilterChip(
            selected = grouping == GroupingMode.BY_CATEGORY,
            onClick = { if (grouping != GroupingMode.BY_CATEGORY) onToggle() },
            label = { Text("Category", maxLines = 1) },
            modifier = Modifier.width(maxWidthDp)
        )
    }
}
