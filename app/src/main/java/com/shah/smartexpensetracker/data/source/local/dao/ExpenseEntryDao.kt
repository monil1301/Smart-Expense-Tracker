package com.shah.smartexpensetracker.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.shah.smartexpensetracker.data.source.local.entity.ExpenseEntity

/**
 * Created by Monil on 09/08/25.
 */

@Dao
interface ExpenseEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)
}
