package com.shah.smartexpensetracker.utils

import com.shah.smartexpensetracker.utils.enums.Category
import com.shah.smartexpensetracker.data.model.Expense
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Created by Monil on 10/08/25.
 */

fun List<Expense>.totalCents(): Long = sumOf { it.amountCents }

fun List<Expense>.totalsPerDayUtc(): Map<LocalDate, Long> =
    groupBy { Instant.ofEpochMilli(it.createdAtMillis).atZone(ZoneOffset.UTC).toLocalDate() }
        .mapValues { (_, items) -> items.sumOf { it.amountCents } }

fun List<Expense>.totalsPerCategory(): Map<Category, Long> =
    groupBy { it.category }.mapValues { (_, items) -> items.sumOf { it.amountCents } }
