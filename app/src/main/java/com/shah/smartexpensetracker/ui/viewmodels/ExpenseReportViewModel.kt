package com.shah.smartexpensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.model.report.CategoryPoint
import com.shah.smartexpensetracker.data.model.report.DayPoint
import com.shah.smartexpensetracker.data.model.report.ReportFormState
import com.shah.smartexpensetracker.data.repository.ExpenseReportRepository
import com.shah.smartexpensetracker.utils.dayBoundsUtcMillis
import com.shah.smartexpensetracker.utils.last7Days
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ExpenseReportRepository
) : ViewModel() {

    private val _range = MutableStateFlow(last7Days()) // Pair<start,end>
    private val _loading = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val expenses: Flow<List<Expense>> = _range.flatMapLatest { (start, end) ->
        val (sMillis, _) = dayBoundsUtcMillis(start)
        val (_, eMillis) = dayBoundsUtcMillis(end)
        repository.getExpensesBetween(sMillis, eMillis)
    }

    val formState: StateFlow<ReportFormState> =
        combine(_range, expenses, _loading) { (start, end), rows, loading ->
            val perDay = buildDaily(start, end, rows)
            val perCat = rows.groupBy { it.category }
                .mapValues { (_, list) -> list.sumOf { it.amountCents } }
                .entries
                .sortedByDescending { it.value }
                .map { CategoryPoint(it.key, it.value) }

            ReportFormState(
                rangeStart = start,
                rangeEnd = end,
                daily = perDay,
                byCategory = perCat,
                grandTotalCents = rows.sumOf { it.amountCents },
                isLoading = loading
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initial())

    fun refresh() = viewModelScope.launch {
        _loading.value = true
        // Flow from Room auto-updates; small delay not needed. Just flip loading briefly.
        _loading.value = false
    }

    fun setRange(start: LocalDate, end: LocalDate) { _range.value = start to end }

    private fun initial(): ReportFormState {
        val (s, e) = last7Days()
        return ReportFormState(rangeStart = s, rangeEnd = e, isLoading = true)
    }

    private fun buildDaily(start: LocalDate, end: LocalDate, rows: List<Expense>): List<DayPoint> {
        val map = rows.groupBy {
            java.time.Instant.ofEpochMilli(it.createdAtMillis)
                .atZone(java.time.ZoneOffset.UTC).toLocalDate()
        }.mapValues { (_, list) -> list.sumOf { it.amountCents } }

        val days = generateSequence(start) { it.plusDays(1) }.takeWhile { !it.isAfter(end) }.toList()
        return days.map { d -> DayPoint(d, map[d] ?: 0L) }
    }
}