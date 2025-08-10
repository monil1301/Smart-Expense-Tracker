package com.shah.smartexpensetracker.di

import com.shah.smartexpensetracker.data.repository.ExpenseEntryRepository
import com.shah.smartexpensetracker.data.source.local.dao.ExpenseEntryDao
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
}