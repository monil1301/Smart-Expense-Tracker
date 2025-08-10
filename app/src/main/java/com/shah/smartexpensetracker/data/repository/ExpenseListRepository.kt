package com.shah.smartexpensetracker.data.repository

import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseListDao
import com.shah.smartexpensetracker.data.source.local.mapper.toDomain
import com.shah.smartexpensetracker.utils.dayBoundsUtcMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

class ExpenseListRepository @Inject constructor(private val dao: ExpenseListDao) {

    fun expensesForDate(date: LocalDate): Flow<List<Expense>> {
        val (start, end) = dayBoundsUtcMillis(date)
        return dao.getForDate(start, end).map { rows -> rows.map { it.toDomain() } }
    }
}
