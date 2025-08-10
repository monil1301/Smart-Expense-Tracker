package com.shah.smartexpensetracker.data.source.local.mapper

import com.shah.smartexpensetracker.utils.enums.Category
import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.source.local.entity.ExpenseEntity

/**
 * Created by Monil on 09/08/25.
 */

fun Expense.toEntity() = ExpenseEntity(
    id, title, amountCents, category.name, notes, receiptUri, createdAtMillis
)

fun ExpenseEntity.toDomain() = Expense(
    id = id,
    title = title,
    amountCents = amountCents,
    category = Category.valueOf(category),
    notes = notes,
    receiptUri = receiptUri,
    createdAtMillis = createdAtMillis
)
