package com.shah.smartexpensetracker.data.repository

import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseReportDao
import com.shah.smartexpensetracker.data.source.local.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

class ExpenseReportRepository @Inject constructor(private val dao: ExpenseReportDao) {
    fun getExpensesBetween(start: Long, end: Long): Flow<List<Expense>> {
        return dao.getBetweenDates(start, end).map { rows -> rows.map { it.toDomain() } }
    }
}
