package com.shah.smartexpensetracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.model.list.ListFormState
import com.shah.smartexpensetracker.data.model.list.Section
import com.shah.smartexpensetracker.data.repository.ExpenseListRepository
import com.shah.smartexpensetracker.utils.AppClock
import com.shah.smartexpensetracker.utils.enums.GroupingMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.*
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val repo: ExpenseListRepository
) : ViewModel() {

    private val _date = MutableStateFlow(AppClock.today())
    private val _grouping = MutableStateFlow(GroupingMode.BY_TIME)
    private val _datePickerOpen = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val expensesForDate: Flow<List<Expense>> =
        _date.flatMapLatest { repo.expensesForDate(it) }

    val formState: StateFlow<ListFormState> =
        combine(
            _date,
            _grouping,
            _datePickerOpen,
            expensesForDate
        ) { date, grouping, dpOpen, items ->
            val sorted = when (grouping) {
                GroupingMode.BY_TIME -> items.sortedBy { it.createdAtMillis }
                GroupingMode.BY_CATEGORY -> items.sortedWith(
                    compareBy<Expense> { it.category.name }.thenBy { it.createdAtMillis }
                )
            }
            val totalCents = sorted.sumOf { it.amountCents }
            val sections = when (grouping) {
                GroupingMode.BY_TIME -> buildTimeSections(sorted, date)
                GroupingMode.BY_CATEGORY -> buildCategorySections(sorted)
            }
            ListFormState(
                date = date,
                grouping = grouping,
                items = sorted,
                totalCount = sorted.size,
                totalCents = totalCents,
                sections = sections,
                isDatePickerOpen = dpOpen
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ListFormState(AppClock.today())
        )

    fun prevDay() = _date.update { it.minusDays(1) }
    fun nextDay() = _date.update { it.plusDays(1) }
    fun setDate(d: LocalDate) {
        _date.value = d
    }

    fun toggleGrouping() = _grouping.update {
        if (it == GroupingMode.BY_TIME) GroupingMode.BY_CATEGORY else GroupingMode.BY_TIME
    }

    fun openDatePicker(open: Boolean) {
        _datePickerOpen.value = open
    }

    private fun buildTimeSections(items: List<Expense>, date: LocalDate): List<Section> {
        if (items.isEmpty()) return emptyList()
        val zone = ZoneOffset.UTC
        val fmt = DateTimeFormatter.ofPattern("HH:mm")
        // Group into hour buckets for a “sectioned list”
        return items.groupBy {
            Instant.ofEpochMilli(it.createdAtMillis).atZone(zone).withMinute(0).withSecond(0)
                .withNano(0)
        }.toSortedMap().map { (hourStart, list) ->
            val title = "${hourStart.toLocalTime().format(fmt)} – ${
                hourStart.plusHours(1).toLocalTime().format(fmt)
            }"
            Section.Time(title = title, expenses = list.sortedBy { it.createdAtMillis })
        }
    }

    private fun buildCategorySections(items: List<Expense>): List<Section> =
        items.groupBy { it.category }
            .toSortedMap(compareBy { it.name })
            .map { (cat, list) ->
                Section.CategorySec(
                    category = cat,
                    title = cat.name.lowercase().replaceFirstChar { it.titlecase() },
                    expenses = list.sortedBy { it.createdAtMillis }
                )
            }
}
