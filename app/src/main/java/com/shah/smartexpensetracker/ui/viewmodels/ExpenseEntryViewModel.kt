package com.shah.smartexpensetracker.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shah.smartexpensetracker.data.model.EntryFormErrors
import com.shah.smartexpensetracker.data.model.EntryFormState
import com.shah.smartexpensetracker.data.model.Expense
import com.shah.smartexpensetracker.data.repository.ExpenseEntryRepository
import com.shah.smartexpensetracker.utils.AppClock
import com.shah.smartexpensetracker.utils.enums.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormatSymbols
import java.time.Instant
import javax.inject.Inject

/**
 * Created by Monil on 10/08/25.
 */

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val repository: ExpenseEntryRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(EntryFormState())
    val formState: StateFlow<EntryFormState> = _formState.asStateFlow()

    // Live "Total Spent Today" (in cents)
    val totalSpentToday: StateFlow<Long> =
        repository.totalTodayCents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onTitleChange(title: String) =
        _formState.update { it.copy(title = title, errors = it.errors.copy(title = null)) }

    fun onAmountChange(amount: String) =
        _formState.update { it.copy(amountInput = amount, errors = it.errors.copy(amount = null)) }

    fun onCategoryChange(category: Category) =
        _formState.update { it.copy(category = category, errors = it.errors.copy(category = null)) }

    fun onNotesChange(notes: String) = _formState.update { it.copy(notes = notes.take(100)) }
    fun onReceipt(uri: Uri?) = _formState.update { it.copy(receiptUri = uri) }

    fun submit() = viewModelScope.launch {
        val now = Instant.now(AppClock.clock).toEpochMilli()
        val entryForm = _formState.value

        val validated = validate(entryForm)
        if (!validated.isValid) {
            _formState.update { it.copy(errors = validated.errors) }; return@launch
        }

        val expense = Expense(
            title = entryForm.title.trim(),
            amountCents = parseCurrencyToCents(entryForm.amountInput),
            category = entryForm.category!!,
            notes = entryForm.notes.take(100).ifBlank { null },
            receiptUri = entryForm.receiptUri?.toString(),
            createdAtMillis = now
        )

        // Non-blocking duplicate hint
        val dup = repository.isDuplicate(expense)
        _formState.update { it.copy(isSubmitting = true, duplicateWarning = dup) }

        repository.addExpense(expense, false)

        // Reset, keep selected category for quick successive entries
        _formState.value = EntryFormState(category = entryForm.category)
        _events.send(UiEvent.Submitted)
    }

    private fun validate(s: EntryFormState): EntryFormState {
        var e = EntryFormErrors()
        if (s.title.isBlank()) e = e.copy(title = "Title cannot be empty")
        val cents = s.amountInput.takeIf { it.isNotBlank() }?.let { parseCurrencyToCentsOrNull(it) }
        if (cents == null || cents <= 0L) e = e.copy(amount = "Enter an amount > 0")
        if (s.category == null) e = e.copy(category = "Pick a category")
        if (s.notes.length > 100) e = e.copy(notes = "Max 100 chars")
        return s.copy(errors = e)
    }

    private fun parseCurrencyToCentsOrNull(input: String): Long? =
        runCatching { parseCurrencyToCents(input) }.getOrNull()

    private fun parseCurrencyToCents(input: String): Long {
        val clean = input.trim()
            .replace("[₹,]".toRegex(), "")
            .replace(DecimalFormatSymbols.getInstance().groupingSeparator.toString(), "")
        val bd = BigDecimal(clean)
        return bd.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact()
    }

    sealed interface UiEvent {
        object Submitted : UiEvent
    }
}