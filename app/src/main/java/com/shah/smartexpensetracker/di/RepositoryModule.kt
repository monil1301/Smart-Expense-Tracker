package com.shah.smartexpensetracker.di

import com.shah.smartexpensetracker.data.repository.ExpenseEntryRepository
import com.shah.smartexpensetracker.data.repository.ExpenseListRepository
import com.shah.smartexpensetracker.data.repository.ExpenseReportRepository
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseEntryDao
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseListDao
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseReportDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Monil on 10/08/25.
 */

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesExpenseEntryRepository(dao: ExpenseEntryDao): ExpenseEntryRepository {
        return ExpenseEntryRepository(dao)
    }

    @Provides
    @Singleton
    fun providesExpenseListRepository(dao: ExpenseListDao): ExpenseListRepository {
        return ExpenseListRepository(dao)
    }

    @Provides
    @Singleton
    fun providesExpenseReportRepository(dao: ExpenseReportDao): ExpenseReportRepository {
        return ExpenseReportRepository(dao)
    }
}
