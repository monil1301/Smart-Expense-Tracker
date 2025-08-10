package com.shah.smartexpensetracker.data.source.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Monil on 09/08/25.
 */

@Entity(tableName = "expenses", indices = [Index("createdAtMillis")])
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amountCents: Long,
    val category: String,      // store enum name
    val notes: String?,
    val receiptUri: String?,
    val createdAtMillis: Long  // epoch millis UTC
)
