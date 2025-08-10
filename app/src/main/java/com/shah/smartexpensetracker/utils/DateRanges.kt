package com.shah.smartexpensetracker.utils

import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Created by Monil on 10/08/25.
 */

fun dayBoundsUtcMillis(date: LocalDate): Pair<Long, Long> {
    val start = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
    val end = date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli() - 1
    return start to end
}

fun last7Days(): Pair<LocalDate, LocalDate> {
    val today = AppClock.today()
    return today.minusDays(6) to today
}
