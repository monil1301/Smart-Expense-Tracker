package com.shah.smartexpensetracker.data.model.list

import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.utils.enums.Category
import com.shah.smartexpensetracker.utils.enums.GroupingMode
import java.time.LocalDate

/**
 * Created by Monil on 10/08/25.
 */

data class ListFormState(
    val date: LocalDate,
    val grouping: GroupingMode = GroupingMode.BY_TIME,
    val items: List<Expense> = emptyList(),
    val totalCount: Int = 0,
    val totalCents: Long = 0L,
    val sections: List<Section> = emptyList(),    // derived for UI
    val isDatePickerOpen: Boolean = false
)

sealed interface Section {
    val title: String
    val expenses: List<Expense>

    data class Time(
        override val title: String,
        override val expenses: List<Expense>
    ) : Section

    data class CategorySec(
        val category: Category,
        override val title: String,
        override val expenses: List<Expense>
    ) : Section
}