package com.skladpro.android.presentation.inventory.details

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun StockOperationDialog(
    type: StockOperationType,
    unit: String,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Double) -> Unit
) {
    var quantityText by rememberSaveable { mutableStateOf("") }
    val quantity = quantityText.replace(',', '.').toDoubleOrNull()
    val isValid = quantity != null && quantity > 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(type.title) },
        text = {
            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Количество, $unit") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (quantity != null) {
                        onConfirm(quantity)
                    }
                },
                enabled = isValid
            ) {
                Text(type.actionLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
