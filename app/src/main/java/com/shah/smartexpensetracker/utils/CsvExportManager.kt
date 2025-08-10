package com.shah.smartexpensetracker.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.shah.smartexpensetracker.data.model.Expense
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

class CsvExportManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneOffset.UTC)
    private val currencyFormatter = java.text.DecimalFormat("#0.00")

    fun writeCsvAndGetUri(
        filePrefix: String,
        rows: List<Expense>
    ): Uri {
        val dir = File(context.cacheDir, "exports").apply { mkdirs() }
        val file =
            File(dir, "${filePrefix}_${dateTimeFormatter.format(java.time.Instant.now())}.csv")

        file.bufferedWriter().use { out ->
            out.appendLine("id,title,amountCents,category,notes,receiptUri,createdAtMillis")
            rows.forEach { e ->
                // naive CSV escaping (quotes double up)
                fun esc(s: String?) = s?.replace("\"", "\"\"") ?: ""
                out.appendLine(
                    listOf(
                        e.id,
                        "\"${esc(e.title)}\"",
                        currencyFormatter.format(e.amountCents / 100.0),
                        e.category.name,
                        "\"${esc(e.notes)}\"",
                        "\"${esc(e.receiptUri)}\"",
                        dateTimeFormatter.format(java.time.Instant.ofEpochMilli(e.createdAtMillis))
                    ).joinToString(",")
                )
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
