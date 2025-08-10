package com.shah.smartexpensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

/**
 * Created by Monil on 10/08/25.
 */

@Composable
fun CategoryBars(
    data: List<Float>,
    labels: List<String>,
    valueFormatter: (Float) -> String,
) {
    if (data.isEmpty()) {
        Text("No categories in this range", style = MaterialTheme.typography.bodyMedium)
        return
    }

    val max = (data.maxOrNull() ?: 0f).coerceAtLeast(1f)
    val barHeight = 20.dp
    val gap = 10.dp

    // Pull colors in composable scope
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val barColor = MaterialTheme.colorScheme.primary

    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        data.indices.forEach { i ->
            Column {
                Text(labels[i], style = MaterialTheme.typography.bodySmall)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(barHeight)
                ) {
                    Canvas(Modifier.matchParentSize()) {
                        // background track
                        drawRect(
                            color = trackColor,
                            size = size
                        )
                        // overlay actual value bar
                        val w = (data[i] / max) * size.width
                        drawRect(
                            color = barColor,
                            size = Size(w, size.height)
                        )
                    }
                }
                Text(valueFormatter(data[i]), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
