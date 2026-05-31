package com.skladpro.android.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skladpro.android.ui.theme.SkladProTheme

@Composable
fun AddInventoryItemScreen(
    title: String,
    subtitle: String,
    actionLabel: String,
    initialDraft: InventoryItemDraft = InventoryItemDraft(),
    onBack: () -> Unit,
    onSave: (InventoryItemDraft) -> Unit
) {
    var draft by remember { mutableStateOf(initialDraft) }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Назад")
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            InventoryTextField("Наименование", draft.name) { draft = draft.copy(name = it) }
            InventoryTextField("Артикул", draft.sku) { draft = draft.copy(sku = it) }
            InventoryTextField("Категория", draft.category) { draft = draft.copy(category = it) }
            InventoryTextField("Единица измерения", draft.unit) { draft = draft.copy(unit = it) }
            InventoryNumberField(
                label = "Текущий остаток",
                value = draft.quantityText,
                isError = draft.quantityText.isNotBlank() &&
                    (draft.quantity?.let { it < 0.0 } != false),
                supportingText = "Значение должно быть не меньше 0"
            ) {
                draft = draft.copy(quantityText = it)
            }
            InventoryNumberField(
                label = "Минимальный остаток",
                value = draft.minQuantityText,
                isError = draft.minQuantityText.isNotBlank() &&
                    (draft.minQuantity?.let { it < 0.0 } != false),
                supportingText = "Значение должно быть не меньше 0"
            ) {
                draft = draft.copy(minQuantityText = it)
            }
            InventoryTextField("Отдел", draft.department) { draft = draft.copy(department = it) }
            InventoryTextField("Стеллаж", draft.shelf) { draft = draft.copy(shelf = it) }
            InventoryTextField("Ячейка", draft.cell) { draft = draft.copy(cell = it) }
            InventoryTextField(
                label = "Дата поставки, если известна",
                value = draft.deliveryDate,
                isError = draft.deliveryDateError != null,
                supportingText = draft.deliveryDateError ?: "Например: 15.06.2026"
            ) {
                draft = draft.copy(deliveryDate = it)
            }
            InventoryNumberField(
                label = "Количество в поставке",
                value = draft.deliveryQuantityText,
                isError = (draft.deliveryDate.isNotBlank() && draft.deliveryQuantityText.isBlank()) ||
                    (draft.deliveryQuantityText.isNotBlank() &&
                        (draft.deliveryQuantity?.let { it <= 0.0 } != false)),
                supportingText = "Для поставки укажите количество больше 0"
            ) {
                draft = draft.copy(deliveryQuantityText = it)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { onSave(draft) },
                enabled = draft.canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun InventoryTextField(
    label: String,
    value: String,
    isError: Boolean = false,
    supportingText: String? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let {
            { Text(it) }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun InventoryNumberField(
    label: String,
    value: String,
    isError: Boolean = false,
    supportingText: String? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = supportingText?.let {
            { Text(it) }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(
    name = "Добавление товара",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun AddInventoryItemScreenPreview() {
    SkladProTheme {
        AddInventoryItemScreen(
            title = "Новый товар",
            subtitle = "Карточка будет добавлена в список запасов",
            actionLabel = "Добавить товар",
            onBack = {},
            onSave = {}
        )
    }
}
