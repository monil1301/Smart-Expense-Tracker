package com.shah.smartexpensetracker.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.shah.smartexpensetracker.data.source.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Monil on 10/08/25.
 */

@Dao
interface ExpenseListDao {
    @Query("""
        SELECT * FROM expenses
        WHERE createdAtMillis BETWEEN :startMillis AND :endMillis
        ORDER BY createdAtMillis ASC
    """)
    fun getForDate(
        startMillis: Long,
        endMillis: Long
    ): Flow<List<ExpenseEntity>>
}
