package com.shah.smartexpensetracker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseEntryDao
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseListDao
import com.shah.smartexpensetracker.data.source.local.entity.ExpenseEntity

/**
 * Created by Monil on 09/08/25.
 */

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseEntryDao(): ExpenseEntryDao
    abstract fun expenseListDao(): ExpenseListDao
}
