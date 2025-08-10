package com.shah.smartexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.shah.smartexpensetracker.data.source.local.ExpenseDatabase
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Monil on 10/08/25.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ExpenseDatabase::class.java,
            name = "expense_db"
        ).build()
    }

    @Provides
    fun providesExpenseEntryDao(database: ExpenseDatabase): ExpenseEntryDao = database.expenseDao()
}
