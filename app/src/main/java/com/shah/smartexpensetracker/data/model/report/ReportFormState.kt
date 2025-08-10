package com.shah.smartexpensetracker.data.model.report

import java.time.LocalDate

/**
 * Created by Monil on 10/08/25.
 */

data class ReportFormState(
    val rangeStart: LocalDate,
    val rangeEnd: LocalDate,
    val daily: List<DayPoint> = emptyList(),           // 7 points, oldest→newest
    val byCategory: List<CategoryPoint> = emptyList(), // non-empty categories only
    val grandTotalCents: Long = 0L,
    val isLoading: Boolean = false
)
