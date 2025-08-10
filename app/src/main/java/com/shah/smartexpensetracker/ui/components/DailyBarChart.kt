package com.shah.smartexpensetracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

/**
 * Created by Monil on 10/08/25.
 */

@Composable
fun DailyBarChart(
    data: List<Float>,
    labels: List<String>, // e.g., ["Mon", "Tue", ...]
    valueFormatter: (Float) -> String,
) {
    if (data.isEmpty()) {
        Text("No data in this range", style = MaterialTheme.typography.bodyMedium)
        return
    }

    val max = (data.maxOrNull() ?: 0f).coerceAtLeast(1f)
    val barColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val labelTextStyle = MaterialTheme.typography.labelSmall

    Column(
        Modifier
            .fillMaxWidth()
            .height(270.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            data.forEach { amount ->
                Text(valueFormatter(amount), style = labelTextStyle)
            }
        }

        // Chart area
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(200.dp) // leaves room for X-axis labels below
        ) {
            val barCount = data.size
            val spacing = 12.dp.toPx()
            val availableWidth = size.width - spacing * (barCount + 1)
            val barWidth = availableWidth / barCount

            // Draw X-axis line
            drawLine(
                color = axisColor,
                start = Offset(0f, size.height - 20.dp.toPx()),
                end = Offset(size.width, size.height - 20.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )

            // Bars
            data.forEachIndexed { i, v ->
                val h = (v / max) * (size.height - 40.dp.toPx()) // leave padding for axis labels
                val left = spacing + i * (barWidth + spacing)
                val top = size.height - 20.dp.toPx() - h
                drawRect(
                    color = barColor,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, h)
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { lbl ->
                Text(lbl, style = labelTextStyle)
            }
        }
    }
}
