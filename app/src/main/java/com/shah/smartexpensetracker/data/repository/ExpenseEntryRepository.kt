package com.shah.smartexpensetracker.data.repository

import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseEntryDao
import com.shah.smartexpensetracker.data.source.local.mapper.toEntity
import com.shah.smartexpensetracker.utils.AppClock
import com.shah.smartexpensetracker.utils.dayBoundsUtcMillis
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

/**
 * Created by Monil on 09/08/25.
 */

class ExpenseEntryRepository @Inject constructor(private val dao: ExpenseEntryDao) {
    suspend fun addExpense(
        expense: Expense,
        rejectIfDuplicate: Boolean,
        duplicateWindowMinutes: Long = 10
    ): Boolean {
        if (rejectIfDuplicate && isDuplicate(expense, duplicateWindowMinutes)) return false
        dao.insert(expense.toEntity())
        return true
    }

    fun totalTodayCents(): Flow<Long> {
        val (start, end) = dayBoundsUtcMillis(AppClock.today())
        return dao.sumBetween(start, end)
    }

    suspend fun isDuplicate(
        candidate: Expense,
        withinMinutes: Long = 10
    ): Boolean {
        val minCreatedAt = Instant.now(AppClock.clock)
            .minus(Duration.ofMinutes(withinMinutes))
            .toEpochMilli()

        val count = dao.countRecentDuplicates(
            title = candidate.title,
            amountCents = candidate.amountCents,
            category = candidate.category.name,
            minCreatedAtMillis = minCreatedAt
        )
        return count > 0
    }
}
