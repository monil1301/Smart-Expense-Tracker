package com.shah.smartexpensetracker.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shah.smartexpensetracker.data.source.local.entity.ExpenseEntity

/**
 * Created by Monil on 09/08/25.
 */

@Dao
interface ExpenseEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Query("""
        SELECT COALESCE(SUM(amountCents), 0) 
        FROM expenses 
        WHERE createdAtMillis BETWEEN :startMillis AND :endMillis
    """)
    fun sumBetween(
        startMillis: Long,
        endMillis: Long
    ): kotlinx.coroutines.flow.Flow<Long>


    @Query("""
        SELECT COUNT(*) FROM expenses
        WHERE LOWER(title) = LOWER(:title)
          AND amountCents = :amountCents
          AND category = :category
          AND createdAtMillis >= :minCreatedAtMillis
    """)
    suspend fun countRecentDuplicates(
        title: String,
        amountCents: Long,
        category: String,
        minCreatedAtMillis: Long
    ): Int
}
