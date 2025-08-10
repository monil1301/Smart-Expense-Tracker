package com.shah.smartexpensetracker.data.model

import android.net.Uri
import com.shah.smartexpensetracker.utils.enums.Category

/**
 * Created by Monil on 10/08/25.
 */

data class EntryFormState(
    val title: String = "",
    val amountInput: String = "",
    val category: Category? = null,
    val notes: String = "",
    val receiptUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val duplicateWarning: Boolean = false,
    val errors: EntryFormErrors = EntryFormErrors()
) {
    val isValid: Boolean get() = errors == EntryFormErrors()
}
