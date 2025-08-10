package com.shah.smartexpensetracker.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shah.smartexpensetracker.ui.viewmodels.EntryViewModel
import com.shah.smartexpensetracker.ui.components.CategoryDropdown
import com.shah.smartexpensetracker.ui.components.EntryTopBar
import com.shah.smartexpensetracker.utils.Constants
import com.shah.smartexpensetracker.utils.displayName
import java.text.NumberFormat
import java.util.Locale


/**
 * Created by Monil on 09/08/25.
 */

@Composable
fun EntryScreen(
    viewModel: EntryViewModel = hiltViewModel(),
    onNavigateToList: () -> Unit,
) {
    val formState by viewModel.formState.collectAsState()
    val total by viewModel.totalSpentToday.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { ev ->
            if (ev is EntryViewModel.UiEvent.Submitted) {
                Toast.makeText(context, "Expense added", Toast.LENGTH_SHORT).show()
                onNavigateToList()
            }
        }
    }

    val currency = remember { NumberFormat.getCurrencyInstance(Locale("en", "IN")) }

    Scaffold(
        topBar = { EntryTopBar(currency, total) },
        bottomBar = {
            Button(
                onClick = viewModel::submit,
                enabled = !formState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add Expense")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            OutlinedTextField(
                value = formState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                isError = formState.errors.title != null,
                supportingText = { formState.errors.title?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            // Amount ₹
            OutlinedTextField(
                value = formState.amountInput,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Amount (₹)") },
                isError = formState.errors.amount != null,
                supportingText = { formState.errors.amount?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            // Category dropdown
            CategoryDropdown(
                value = formState.category?.displayName() ?: "",
                category = formState.errors.category,
                isError = formState.errors.category != null
            ) {
                viewModel.onCategoryChange(it)
            }

            // Notes (<= 100)
            OutlinedTextField(
                value = formState.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes (optional)") },
                supportingText = {
                    Text("${formState.notes.length}/100")
                    formState.errors.notes?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Receipt image
            val pickImage = rememberLauncherForActivityResult(GetContent()) { uri ->
                viewModel.onReceipt(uri)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { pickImage.launch("image/*") }) { Text("Pick Receipt") }
                OutlinedButton(onClick = { viewModel.onReceipt(Constants.ImageUrls.dummyReceipt.toUri()) }) {
                    Text(
                        "Mock Image"
                    )
                }
                AnimatedVisibility(
                    visible = formState.duplicateWarning,
                    enter = scaleIn(spring())
                ) {
                    AssistChip(onClick = {}, label = { Text("Looks similar to a recent entry") })
                }
            }

            formState.receiptUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Receipt preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }
        }
    }
}
