package com.shah.smartexpensetracker.data.model

import java.time.Instant
import java.util.UUID

/**
 * Created by Monil on 09/08/25.
 */

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amountCents: Long,
    val category: Category,
    val notes: String? = null,
    val receiptUri: String? = null,
    val createdAtMillis: Long = Instant.now().toEpochMilli()
)

enum class Category { STAFF, TRAVEL, FOOD, UTILITY }
