package com.skladpro.android.presentation.inventory.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.skladpro.android.domain.model.InventoryItem
import com.skladpro.android.presentation.inventory.validateDeliveryDate

@Composable
fun DeliveryDialog(
    item: InventoryItem,
    onDismiss: () -> Unit,
    onConfirm: (date: String?, quantity: Double?) -> Unit
) {
    var dateText by rememberSaveable { mutableStateOf(item.expectedDeliveryDate.orEmpty()) }
    var quantityText by rememberSaveable {
        mutableStateOf(item.expectedDeliveryQuantity?.let(::formatQuantity).orEmpty())
    }
    val quantity = quantityText.replace(',', '.').toDoubleOrNull()
    val dateError = validateDeliveryDate(dateText)
    val quantityError = quantityText.isNotBlank() && (quantity == null || quantity <= 0.0)
    val hasDate = dateText.isNotBlank()
    val hasQuantity = quantityText.isNotBlank()
    val deliveryFieldsAreValid = (!hasDate && !hasQuantity) ||
        (hasDate && quantity != null && quantity > 0.0)
    val canSave = dateError == null && !quantityError && deliveryFieldsAreValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ожидаемая поставка") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Дата поставки") },
                    placeholder = { Text("Например, 15.06.2026") },
                    singleLine = true,
                    isError = dateError != null,
                    supportingText = {
                        Text(dateError ?: "Например: 15.06.2026")
                    }
                )
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Количество, ${item.unit}") },
                    singleLine = true,
                    isError = quantityError || (hasDate && !hasQuantity),
                    supportingText = {
                        Text("Для поставки укажите количество больше 0")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        dateText.trim().ifBlank { null },
                        quantity
                    )
                },
                enabled = canSave
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

private fun formatQuantity(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString()
    }
}
