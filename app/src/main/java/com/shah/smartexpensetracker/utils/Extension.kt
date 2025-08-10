package com.shah.smartexpensetracker.utils

import com.shah.smartexpensetracker.utils.enums.Category

/**
 * Created by Monil on 10/08/25.
 */

fun Category.displayName(): String = when (this) {
    Category.STAFF -> "Staff"
    Category.TRAVEL -> "Travel"
    Category.FOOD -> "Food"
    Category.UTILITY -> "Utility"
}
